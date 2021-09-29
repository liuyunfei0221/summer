package com.blue.caffeine.api.conf;

import com.blue.caffeine.constant.ExpireStrategy;

import java.time.Duration;
import java.util.concurrent.ExecutorService;

/**
 * caffeine conf
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface CaffeineConf {

    /**
     * max size
     *
     * @return
     */
    Integer getMaximumSize();

    /**
     * expire
     *
     * @return
     */
    Duration getExpireDuration();

    /**
     * expire strategy
     *
     * @return
     */
    ExpireStrategy getExpireStrategy();

    /**
     * executor pool
     *
     * @return
     */
    ExecutorService getExecutorService();

}
