package com.blue.identity.core;

import com.blue.identity.core.param.IdBufferParam;
import com.blue.identity.core.param.IdGenParam;
import com.blue.identity.core.param.SnowIdGenParam;
import com.blue.identity.model.IdentityElement;
import reactor.util.Logger;

import java.util.concurrent.ExecutorService;

import static com.blue.identity.constant.SnowflakeBits.SEQUENCE;
import static com.blue.identity.constant.SnowflakeBufferThreshold.*;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * buffered generator
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public final class BlueIdentityGenerator {

    private static final Logger LOGGER = getLogger(BlueIdentityGenerator.class);

    /**
     * buffer
     */
    private final SnowflakeIdentityBuffer snowflakeIdentityBuffer;

    /**
     * constructor
     *
     * @param idGenParam
     */
    public BlueIdentityGenerator(IdGenParam idGenParam) {
        LOGGER.info("BlueIdentityGenerator init, idGenParam = {}", idGenParam);

        int sequenceBits = SEQUENCE.len;

        ExecutorService executorService = idGenParam.getExecutorService();
        
        int bufferSize = ((int) ~(-1L << sequenceBits) + 1) <<
                ofNullable(idGenParam.getBufferPower()).filter(p -> p >= MIN_POWER.threshold && p <= MAX_POWER.threshold).orElse(DEFAULT_POWER.threshold);
        this.snowflakeIdentityBuffer = new SnowflakeIdentityBuffer(new IdBufferParam(new SnowflakeIdentityGenerator(new SnowIdGenParam(idGenParam.getDataCenter(), idGenParam.getWorker(), idGenParam.getLastSeconds(),
                idGenParam.getBootSeconds(), idGenParam.getSecondsRecorder(), idGenParam.getRecordInterval(), idGenParam.getMaximumTimeAlarm(), executorService)), executorService, idGenParam.getPaddingScheduled(),
                idGenParam.getScheduledExecutorService(), idGenParam.getPaddingScheduledInitialDelayMillis(), idGenParam.getPaddingScheduledDelayMillis(), bufferSize,
                ofNullable(idGenParam.getPaddingFactor()).filter(f -> f >= MIN_PADDING_FACTOR.threshold && f <= MAX_PADDING_FACTOR.threshold).orElse(DEFAULT_PADDING_FACTOR.threshold)));

        LOGGER.info("Initialized BlueIdentityGenerator successfully, idGenParam = {}", idGenParam);
    }

    /**
     * Get id
     *
     * @return
     */
    public long generate() {
        return snowflakeIdentityBuffer.take();
    }

    /**
     * Parse id
     *
     * @param id
     * @return
     */
    public IdentityElement parse(long id) {
        return SnowflakeIdentityParser.parse(id);
    }

}
