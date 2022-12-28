package com.blue.identity.core;

import com.blue.identity.core.exp.IdentityException;
import com.blue.identity.core.param.IdBufferParam;
import reactor.util.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.identity.constant.SnowflakeBufferThreshold.MAX_PADDING_FACTOR;
import static com.blue.identity.constant.SnowflakeBufferThreshold.MIN_PADDING_FACTOR;
import static java.lang.Integer.bitCount;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static reactor.util.Loggers.getLogger;


/**
 * id buffer container
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaThreadPoolCreation", "JavaDoc", "ControlFlowStatementWithoutBraces", "AliControlFlowStatementWithoutBraces"})
public final class SnowflakeIdentityBuffer {

    private static final Logger LOGGER = getLogger(SnowflakeIdentityBuffer.class);

    private final SnowflakeIdentityGenerator snowflakeIdentityGenerator;

    /**
     * is id valid?
     */
    private static final long INVALID = -1L;

    /**
     * max index
     */
    private final long indexMask;

    /**
     * buffer
     */
    private final IdentityAtomicLong[] slots;

    /**
     * pointer
     */
    private final IdentityAtomicLong
            head = new IdentityAtomicLong(0L),
            tail = new IdentityAtomicLong(0L);

    /**
     * padding mark
     */
    private final IdentityAtomicBoolean paddingFlag = new IdentityAtomicBoolean(false);

    /**
     * threshold for padding
     */
    private final int paddingThreshold;

    /**
     * padding executors
     */
    private final ExecutorService bufferPadExecutor;

    /**
     * constructor
     *
     * @param idBufferParam
     */
    public SnowflakeIdentityBuffer(IdBufferParam idBufferParam) {
        LOGGER.info("SnowflakeIdentityBuffer init, idBufferParam = {}", idBufferParam);

        SnowflakeIdentityGenerator snowflakeIdentityGenerator = idBufferParam.getSnowflakeIdentityGenerator();
        if (isNull(snowflakeIdentityGenerator))
            throw new IdentityException("blueIdentityGenerator must not be null");
        this.snowflakeIdentityGenerator = snowflakeIdentityGenerator;

        ExecutorService bufferPadExecutor = idBufferParam.getBufferPadExecutor();
        if (isNull(bufferPadExecutor))
            throw new IdentityException("bufferPadExecutor must not be null");
        this.bufferPadExecutor = bufferPadExecutor;

        int bufferSize = idBufferParam.getBufferSize();
        int paddingFactor = idBufferParam.getPaddingFactor();
        if (bufferSize < 0L || bitCount(bufferSize) != 1)
            throw new IdentityException("bufferSize must be positive and a power of 2");
        if (paddingFactor < MIN_PADDING_FACTOR.threshold || paddingFactor > MAX_PADDING_FACTOR.threshold)
            throw new IdentityException("paddingFactor range must be in (" + MIN_PADDING_FACTOR.threshold + " - " + MAX_PADDING_FACTOR.threshold + ")");

        this.indexMask = (long) bufferSize - 1L;
        this.slots = new IdentityAtomicLong[bufferSize];

        for (int i = 0; i < bufferSize; i++)
            this.slots[i] = new IdentityAtomicLong(INVALID);
        this.paddingThreshold = bufferSize * paddingFactor / 100;

        if (ofNullable(idBufferParam.getPaddingScheduled()).orElse(false)) {
            ScheduledExecutorService bufferPadSchedule = idBufferParam.getBufferPadSchedule();
            if (isNull(bufferPadSchedule))
                throw new IdentityException("when paddingScheduled is true, bufferPadSchedule must not be null");

            long paddingScheduledInitialDelayMillis = ofNullable(idBufferParam.getPaddingScheduledInitialDelayMillis()).orElse(-1L);
            if (paddingScheduledInitialDelayMillis < 0L)
                throw new IdentityException("when paddingScheduled is true, paddingScheduledInitialDelay can't be less than 0");

            long paddingScheduledDelayMillis = ofNullable(idBufferParam.getPaddingScheduledDelayMillis()).orElse(-1L);
            if (paddingScheduledDelayMillis < 1L)
                throw new IdentityException("when paddingScheduled is true, paddingScheduledDelay can't be less than 1");

            bufferPadSchedule.scheduleWithFixedDelay(this::asyncPadding, paddingScheduledInitialDelayMillis, paddingScheduledDelayMillis, MILLISECONDS);
        }

        this.padding();
        LOGGER.info("Initialized BlueIdentityBuffer successfully, idBufferParam = {}, indexMask = {}, slots.length = {}, paddingThreshold = {}", idBufferParam, indexMask, slots.length, paddingThreshold);
    }

    /**
     * index of position
     *
     * @param sequence
     * @return
     */
    private int index(long sequence) {
        return (int) (sequence & indexMask);
    }

    /**
     * padding
     */
    private void padding() {
        if (!paddingFlag.compareAndSet(false, true))
            return;

        long curTail;
        for (; ; ) {
            curTail = tail.get();

            if (curTail - head.get() >= indexMask)
                break;

            if (slots[index(curTail + 1)].compareAndSet(INVALID, snowflakeIdentityGenerator.generate())) {
                tail.incrementAndGet();
                continue;
            }

            break;
        }

        paddingFlag.compareAndSet(true, false);
    }

    /**
     * padding async
     */
    private void asyncPadding() {
        if (!paddingFlag.get())
            bufferPadExecutor.submit(this::padding);
    }

    /**
     * padding async
     */
    private void asyncPaddingWithThreshold(long head, long tail) {
        bufferPadExecutor.submit(() -> {
            if (!paddingFlag.get() && (int) (tail - head) < paddingThreshold)
                asyncPadding();
        });
    }

    /**
     * take id
     *
     * @return
     */
    public long take() {
        long curHead = head.get();
        long curTail = tail.get();
        long nextHead = head.updateAndGet(old -> old != curTail ? old + 1 : old);

        asyncPaddingWithThreshold(curHead, curTail);

        return nextHead != curHead ?
                slots[index(nextHead)].getAndUpdate(old -> INVALID)
                :
                snowflakeIdentityGenerator.generate();
    }

}
