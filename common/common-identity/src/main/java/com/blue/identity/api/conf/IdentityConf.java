package com.blue.identity.api.conf;

import java.util.function.Consumer;

/**
 * identity conf
 *
 * @author DarkBlue
 */

@SuppressWarnings("JavaDoc")
public interface IdentityConf {

    /**
     * datacenter no
     *
     * @return
     */
    Integer getDataCenter();

    /**
     * worker no
     *
     * @return
     */
    Integer getWorker();

    /**
     * service name
     *
     * @return
     */
    String getServiceName();

    /**
     * last id generated seconds
     *
     * @return
     */
    Long getLastSeconds();

    /**
     * online seconds
     *
     * @return
     */
    Long getBootSeconds();

    /**
     * alarm for timestamp reach max
     *
     * @return
     */
    Consumer<Long> getMaximumTimeAlarm();

    /**
     * last id generated seconds recorder
     *
     * @return
     */
    Consumer<Long> getSecondsRecorder();

    /**
     * buffer size (power of 2)
     *
     * @return
     */
    Integer getBufferPower();

    /**
     * threshold for padding (percentage)
     *
     * @return
     */
    Integer getPaddingFactor();

    /**
     * async padding executor core pool size
     *
     * @return
     */
    Integer getPaddingCorePoolSize();

    /**
     * async padding executor max pool size
     *
     * @return
     */
    Integer getPaddingMaximumPoolSize();

    /**
     * async padding executor max pool keep alive seconds
     *
     * @return
     */
    Long getKeepAliveSeconds();

    /**
     * async padding executor max pool blocking queue size
     *
     * @return
     */
    Integer getPaddingBlockingQueueSize();

    /**
     * enable scheduled padding
     *
     * @return
     */
    Boolean getPaddingScheduled();

    /**
     * scheduled padding executor core pool size
     *
     * @return
     */
    Integer getPaddingScheduledCorePoolSize();

    /**
     * scheduled padding init delay millis
     *
     * @return
     */
    Long getPaddingScheduledInitialDelayMillis();

    /**
     * scheduled padding interval delay millis
     *
     * @return
     */
    Long getPaddingScheduledDelayMillis();

}
