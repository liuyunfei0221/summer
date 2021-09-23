package com.blue.identity.core;

import com.blue.identity.core.exp.IdentityException;
import com.blue.identity.core.param.IdBufferParam;
import reactor.util.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

import static com.blue.identity.constant.SnowflakeBufferThreshold.MAX_PADDING_FACTOR;
import static com.blue.identity.constant.SnowflakeBufferThreshold.MIN_PADDING_FACTOR;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static reactor.util.Loggers.getLogger;


/**
 * id buffer container
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AlibabaThreadPoolCreation", "JavaDoc", "ControlFlowStatementWithoutBraces", "AliControlFlowStatementWithoutBraces"})
public final class SnowflakeIdentityBuffer {

    private static final Logger LOGGER = getLogger(SnowflakeIdentityBuffer.class);

    private final SnowflakeIdentityGenerator snowflakeIdentityGenerator;

    /**
     * id is valid?
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
    private final IdentityAtomicLong head = new IdentityAtomicLong(0L);
    private final IdentityAtomicLong tail = new IdentityAtomicLong(0L);

    /**
     * padding mark
     */
    private final IdentityAtomicBoolean padding = new IdentityAtomicBoolean(false);

    /**
     * threshold for padding
     */
    private final int paddingThreshold;

    /**
     * padding executors
     */
    private final ExecutorService bufferPadExecutor;
    @SuppressWarnings({"FieldCanBeLocal"})
    private final ScheduledExecutorService bufferPadSchedule;

    /**
     * constructor
     *
     * @param idBufferParam
     */
    public SnowflakeIdentityBuffer(IdBufferParam idBufferParam) {
        LOGGER.info("SnowflakeIdentityBuffer init, idBufferParam = {}", idBufferParam);

        SnowflakeIdentityGenerator snowflakeIdentityGenerator = idBufferParam.getSnowflakeIdentityGenerator();
        if (snowflakeIdentityGenerator == null)
            throw new IdentityException("blueIdentityGenerator must not be null");
        this.snowflakeIdentityGenerator = snowflakeIdentityGenerator;

        ExecutorService bufferPadExecutor = idBufferParam.getBufferPadExecutor();
        if (bufferPadExecutor == null)
            throw new IdentityException("bufferPadExecutor must not be null");
        this.bufferPadExecutor = bufferPadExecutor;

        int bufferSize = idBufferParam.getBufferSize();
        int paddingFactor = idBufferParam.getPaddingFactor();
        if (bufferSize < 0L || Integer.bitCount(bufferSize) != 1)
            throw new IdentityException("bufferSize must be positive and a power of 2");
        if (paddingFactor < MIN_PADDING_FACTOR.threshold || paddingFactor > MAX_PADDING_FACTOR.threshold)
            throw new IdentityException("paddingFactor range must be in (" + MIN_PADDING_FACTOR.threshold + " - " + MAX_PADDING_FACTOR.threshold + ")");

        this.indexMask = (long) bufferSize - 1L;
        this.slots = new IdentityAtomicLong[bufferSize];

        for (int i = 0; i < bufferSize; i++)
            this.slots[i] = new IdentityAtomicLong(INVALID);
        this.paddingThreshold = bufferSize * paddingFactor / 100;

        this.bufferPadSchedule = idBufferParam.getBufferPadSchedule();
        if (ofNullable(idBufferParam.getPaddingScheduled()).orElse(false)) {
            if (this.bufferPadSchedule == null)
                throw new IdentityException("when paddingScheduled is true, bufferPadSchedule must not be null");

            long paddingScheduledInitialDelayMillis = ofNullable(idBufferParam.getPaddingScheduledInitialDelayMillis()).orElse(-1L);
            if (paddingScheduledInitialDelayMillis < 0L)
                throw new IdentityException("when paddingScheduled is true, paddingScheduledInitialDelay can't be less than 0");

            long paddingScheduledDelayMillis = ofNullable(idBufferParam.getPaddingScheduledDelayMillis()).orElse(-1L);
            if (paddingScheduledDelayMillis < 1L)
                throw new IdentityException("when paddingScheduled is true, paddingScheduledDelay can't be less than 1");

            this.bufferPadSchedule.scheduleWithFixedDelay(this::paddingBuffer, paddingScheduledInitialDelayMillis, paddingScheduledDelayMillis, MILLISECONDS);
        }

        this.paddingBuffer();
        LOGGER.info("Initialized BlueIdentityBuffer successfully, idBufferParam = {}, indexMask = {}, slots.length = {}, paddingThreshold = {}", idBufferParam, indexMask, slots.length, paddingThreshold);
    }


    /**
     * padding async
     */
    private void asyncPadding(long head, long tail) {
        bufferPadExecutor.submit(() -> {
            if (tail - head < paddingThreshold)
                paddingBuffer();
        });
    }

    /**
     * padding
     */
    private void paddingBuffer() {
        if (!padding.compareAndSet(false, true))
            return;

        //noinspection StatementWithEmptyBody
        while (put()) {
        }

        padding.compareAndSet(true, false);
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
     * take id
     *
     * @return
     */
    public long take() {
        long curHead = head.get();
        long curTail = tail.get();
        long nextHead = head.updateAndGet(old -> old != curTail ? old + 1 : old);

        asyncPadding(nextHead, curTail);

        if (nextHead != curHead)
            return slots[index(nextHead)].getAndUpdate(old -> INVALID);

        return snowflakeIdentityGenerator.generate();
    }

    /**
     * put id
     *
     * @return
     */
    private synchronized boolean put() {
        long curTail = tail.get();

        if (curTail - head.get() == indexMask)
            return false;

        if (slots[index(curTail + 1)].compareAndSet(INVALID, snowflakeIdentityGenerator.generate())) {
            tail.incrementAndGet();
            return true;
        }

        return false;
    }

}
