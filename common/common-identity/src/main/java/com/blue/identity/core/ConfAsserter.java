package com.blue.identity.core;

import com.blue.identity.api.conf.IdentityConf;
import com.blue.identity.core.exp.IdentityException;
import reactor.util.Logger;

import java.util.function.Consumer;

import static com.blue.identity.constant.SnowflakeBits.*;
import static com.blue.identity.constant.SnowflakeBufferThreshold.MAX_PADDING_FACTOR;
import static com.blue.identity.constant.SnowflakeBufferThreshold.MIN_PADDING_FACTOR;
import static java.lang.System.currentTimeMillis;
import static java.time.Instant.now;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static reactor.util.Loggers.getLogger;

/**
 * asserter
 *
 * @author liuyunfei
 * @date 2021/9/2
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "AlibabaMethodTooLong"})
public final class ConfAsserter {

    private static final Logger LOGGER = getLogger(ConfAsserter.class);

    /**
     * Legal length
     */
    private static final int BITS_64 = 64;

    /**
     * random advance time
     */
    private static final long MAX_RAN_ADVANCE_SEC_BOUND = 120L;

    /**
     * 校验配置项
     *
     * @param identityConf
     */
    public static void assertConf(IdentityConf identityConf) {
        LOGGER.info("void assertConf(IdentityConf identityConf), identityConf = {}", identityConf);

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
        Integer dataCenter = identityConf.getDataCenter();
        Integer worker = identityConf.getWorker();

        if (dataCenter == null || dataCenter.longValue() > maxDataCenter)
            throw new IdentityException("dataCenter cannot be null or greater than " + maxDataCenter);
        if (worker == null || worker.longValue() > maxWorker)
            throw new IdentityException("maxWorker cannot be null or greater than " + maxWorker);

        String serviceName = identityConf.getServiceName();
        if (serviceName == null || "".equals(serviceName))
            throw new IdentityException("serviceName cannot be null or ''");

        long maxStepTimestamp = ~(-1L << timestampBits);

        long currentSecond = MILLISECONDS.toSeconds(currentTimeMillis());
        Long bootSeconds = identityConf.getBootSeconds();
        long lastSeconds = ofNullable(identityConf.getLastSeconds())
                .filter(ls -> ls > 0)
                .orElse(now().getEpochSecond());

        if (bootSeconds == null || bootSeconds >= currentSecond || bootSeconds >= lastSeconds)
            throw new IdentityException("bootSeconds cannot be greater than " + currentSecond + " or " + lastSeconds);

        long stepSeconds = lastSeconds - bootSeconds + MAX_RAN_ADVANCE_SEC_BOUND;
        if (stepSeconds > maxStepTimestamp)
            throw new IdentityException("stepSeconds cannot be greater than " + maxStepTimestamp);

        Consumer<Long> maximumTimeAlarm = identityConf.getMaximumTimeAlarm();
        if (maximumTimeAlarm == null)
            throw new IdentityException("maximumTimeAlarm can't be null");

        Integer paddingFactor = identityConf.getPaddingFactor();
        if (paddingFactor == null || paddingFactor < MIN_PADDING_FACTOR.threshold || paddingFactor > MAX_PADDING_FACTOR.threshold)
            throw new IdentityException("paddingFactor range must be in (" + MIN_PADDING_FACTOR.threshold + " - " + MAX_PADDING_FACTOR.threshold + ")");

        Integer paddingCorePoolSize = identityConf.getPaddingCorePoolSize();
        if (paddingCorePoolSize == null || paddingCorePoolSize < 1)
            throw new IdentityException("paddingCorePoolSize can't be null or less than 1");

        Integer paddingMaximumPoolSize = identityConf.getPaddingMaximumPoolSize();
        if (paddingMaximumPoolSize == null || paddingMaximumPoolSize < 1)
            throw new IdentityException("paddingMaximumPoolSize can't be null or less than 1");

        if (paddingMaximumPoolSize < paddingCorePoolSize)
            throw new IdentityException("paddingMaximumPoolSize can't less than paddingCorePoolSize");

        Long keepAliveSeconds = identityConf.getKeepAliveSeconds();
        if (keepAliveSeconds == null || keepAliveSeconds < 1L)
            throw new IdentityException("keepAliveSeconds can't be null or less than 1");

        Integer paddingBlockingQueueSize = identityConf.getPaddingBlockingQueueSize();
        if (paddingBlockingQueueSize == null || paddingBlockingQueueSize < 1)
            throw new IdentityException("paddingBlockingQueueSize can't be null or less than 1");

        if (ofNullable(identityConf.getPaddingScheduled()).orElse(false)) {
            Integer paddingScheduledCorePoolSize = identityConf.getPaddingScheduledCorePoolSize();
            if (paddingScheduledCorePoolSize == null || paddingScheduledCorePoolSize < 1)
                throw new IdentityException("paddingScheduledCorePoolSize can't be null or less than 1");

            Long paddingScheduledInitialDelayMillis = identityConf.getPaddingScheduledInitialDelayMillis();
            if (paddingScheduledInitialDelayMillis == null || paddingScheduledInitialDelayMillis < 1)
                throw new IdentityException("paddingScheduledInitialDelayMillis can't be null or less than 1");

            Long paddingScheduledDelayMillis = identityConf.getPaddingScheduledDelayMillis();
            if (paddingScheduledDelayMillis == null || paddingScheduledDelayMillis < 1)
                throw new IdentityException("paddingScheduledDelayMillis can't be null or less than 1");
        }

    }

}
