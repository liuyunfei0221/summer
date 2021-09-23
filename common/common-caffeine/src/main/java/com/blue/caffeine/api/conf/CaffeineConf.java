package com.blue.caffeine.api.conf;

import com.blue.caffeine.constant.ExpireStrategy;

import java.time.Duration;
import java.util.concurrent.ExecutorService;

/**
 * caffeine配置参数封装
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface CaffeineConf {

    /**
     * 最大缓存量
     *
     * @return
     */
    Integer getMaximumSize();

    /**
     * 过期时间
     *
     * @return
     */
    Duration getExpireDuration();

    /**
     * 过期策略
     *
     * @return
     */
    ExpireStrategy getExpireStrategy();

    /**
     * 线程池
     *
     * @return
     */
    ExecutorService getExecutorService();

}
