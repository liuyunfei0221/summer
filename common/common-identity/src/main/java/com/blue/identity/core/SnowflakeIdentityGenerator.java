package com.blue.identity.core;

import com.blue.identity.core.exp.IdentityException;
import com.blue.identity.core.param.SnowIdGenParam;
import reactor.util.Logger;

import java.time.Instant;
import java.time.ZoneId;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.base.constant.base.SummerAttr.DATE_TIME_FORMATTER;
import static com.blue.base.constant.base.SummerAttr.TIME_ZONE;
import static com.blue.identity.constant.SnowflakeBits.*;
import static java.lang.System.currentTimeMillis;
import static java.time.Instant.now;
import static java.time.LocalDateTime.ofInstant;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.ThreadLocalRandom.current;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static reactor.util.Loggers.getLogger;


/**
 * sync id generator by snowflake
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
public final class SnowflakeIdentityGenerator {

    private static final Logger LOGGER = getLogger(SnowflakeIdentityGenerator.class);

    /**
     * Legal length
     */
    private static final int BITS_64 = 64;
    /**
     * random advance time
     */
    private static final long RAN_ADVANCE_SEC_BOUND = 120L;

    /**
     * online seconds
     */
    private long bootSeconds;

    /**
     * max timeStamp,max sequence
     */
    private long maxStepTimestamp;
    private final long maxSequence;

    /**
     * current sequence,last timeStamp
     */
    private long sequence;
    private long stepSeconds;

    /**
     * The offset and mask used to generate the ID
     */
    private final int timestampShift;
    private final long dataCenterWithWorkerBitsMask;

    /**
     * maximum time alarm
     */
    private Consumer<Long> maximumTimeAlarm;

    /**
     * need to record last seconds?
     */
    private boolean notRecord;

    /**
     * last seconds recorder
     */
    private Consumer<Long> secondsRecorder;

    private ExecutorService executorService;

    public SnowflakeIdentityGenerator(SnowIdGenParam snowIdGenParam) {
        LOGGER.info("BlueIdentityGenerator init, snowIdGenParam = {}", snowIdGenParam);

        int timestampBits = TIME_STAMP.len;
        int dataCenterBits = DATA_CENTER.len;
        int workerBits = WORKER.len;
        int sequenceBits = SEQUENCE.len;

        if (timestampBits < 1 || dataCenterBits < 1 || workerBits < 1 || sequenceBits < 1)
            throw new IdentityException("timestampBits,dataCenterBits,workerBits,sequenceBits must be greater than 0");
        if (BITS_64 != (1 + timestampBits + dataCenterBits + workerBits + sequenceBits))
            throw new IdentityException("sum of (timestampBits,dataCenterBits,workerBits,sequenceBits) must be 63");

        long maxDataCenter = ~(-1L << dataCenterBits);
        long maxWorker = ~(-1L << workerBits);
        long dataCenter = snowIdGenParam.getDataCenter();
        long worker = snowIdGenParam.getWorker();

        if (dataCenter > maxDataCenter)
            throw new IdentityException("dataCenter cannot be greater than " + maxDataCenter);
        if (worker > maxWorker)
            throw new IdentityException("maxWorker cannot be greater than " + maxWorker);

        this.maxStepTimestamp = ~(-1L << timestampBits);

        long currentSecond = MILLISECONDS.toSeconds(currentTimeMillis());
        this.bootSeconds = snowIdGenParam.getBootSeconds();
        long lastSeconds = ofNullable(snowIdGenParam.getLastSeconds())
                .filter(ls -> ls > 0)
                .orElseGet(() -> now().getEpochSecond());

        if (bootSeconds >= currentSecond || bootSeconds >= lastSeconds)
            throw new IdentityException("bootSeconds cannot be greater than " + currentSecond + " or " + lastSeconds);

        long randomOffset = current().nextLong(RAN_ADVANCE_SEC_BOUND);
        this.stepSeconds = lastSeconds - bootSeconds + randomOffset;

        if (stepSeconds > maxStepTimestamp)
            throw new IdentityException("stepSeconds cannot be greater than " + maxStepTimestamp);

        this.maxSequence = ~(-1L << sequenceBits);
        this.sequence = 0L;

        this.timestampShift = dataCenterBits + workerBits + sequenceBits;
        int dataCenterShift = workerBits + sequenceBits;
        this.dataCenterWithWorkerBitsMask = (dataCenter << dataCenterShift) | (worker << sequenceBits);

        this.executorService = snowIdGenParam.getExecutorService();
        if (this.executorService == null)
            throw new IdentityException("executorService can't be null");

        this.maximumTimeAlarm = snowIdGenParam.getMaximumTimeAlarm();
        if (maximumTimeAlarm == null)
            throw new IdentityException("maximumTimeAlarm can't be null");

        this.secondsRecorder = snowIdGenParam.getSecondsRecorder();
        this.notRecord = this.secondsRecorder == null;

        ZoneId zoneId = ZoneId.of(TIME_ZONE);
        LOGGER.info(
                "Initialized BlueIdentityBuffer successfully, snowIdGenParam = {}, dataCenter = {}, worker = {}, maxStepTimestamp = {}, maxSequence = {}, sequence = {}, stepSeconds = {}, bootTime = {}, lastTime = {}, stepTime = {}",
                snowIdGenParam, dataCenter, worker, maxStepTimestamp, maxSequence, sequence, this.stepSeconds,
                ofInstant(Instant.ofEpochSecond(bootSeconds), zoneId).format(DATE_TIME_FORMATTER),
                ofInstant(Instant.ofEpochSecond(lastSeconds), zoneId).format(DATE_TIME_FORMATTER),
                ofInstant(Instant.ofEpochSecond(stepSeconds), zoneId).format(DATE_TIME_FORMATTER)
        );
    }

    /**
     * alarm for Maximum time to reach
     */
    private final Consumer<Long> MAXIMUM_TIME_ALARM = stepSeconds -> {
        if (stepSeconds > maxStepTimestamp) {
            long maxTimeStamp = maxStepTimestamp + bootSeconds;
            LOGGER.error("Maximum time to reach {}", maxTimeStamp);
            maximumTimeAlarm.accept(maxTimeStamp);
        }
    };

    /**
     * record last seconds
     */
    private final Consumer<Long> STEP_SECONDS_RECORDER = stepSeconds -> {
        if (notRecord)
            return;
        secondsRecorder.accept(stepSeconds + bootSeconds);
    };

    /**
     * last seconds processor
     */
    private final Consumer<Long> LAST_SECONDS_PROCESSOR = stepSeconds ->
            executorService.execute(() -> {
                MAXIMUM_TIME_ALARM.accept(stepSeconds);
                STEP_SECONDS_RECORDER.accept(stepSeconds);
            });

    /**
     * generate a snowflake id
     *
     * @return
     */
    public long generate() {
        final long seq;
        final long timeStamp;
        boolean stepAdvanced = false;
        synchronized (this) {
            sequence = (sequence + 1) & maxSequence;
            if (sequence == 0) {
                ++stepSeconds;
                stepAdvanced = true;
            }
            seq = sequence;
            timeStamp = stepSeconds;
        }

        if (stepAdvanced)
            LAST_SECONDS_PROCESSOR.accept(timeStamp);

        return (timeStamp << timestampShift) | dataCenterWithWorkerBitsMask | seq;
    }

}
