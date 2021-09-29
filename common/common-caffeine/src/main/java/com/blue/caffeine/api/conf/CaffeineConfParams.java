package com.blue.caffeine.api.conf;

import com.blue.caffeine.constant.ExpireStrategy;

import java.time.Duration;
import java.util.concurrent.ExecutorService;

/**
 * caffeine conf params
 *
 * @author liuyunfei
 * @date 2021/9/11
 * @apiNote
 */
@SuppressWarnings("unused")
public class CaffeineConfParams implements CaffeineConf {

    protected Integer maximumSize;

    protected Duration expireDuration;

    protected ExpireStrategy expireStrategy;

    protected ExecutorService executorService;

    public CaffeineConfParams() {
    }

    public CaffeineConfParams(Integer maximumSize, Duration expireDuration, ExpireStrategy expireStrategy, ExecutorService executorService) {
        this.maximumSize = maximumSize;
        this.expireDuration = expireDuration;
        this.expireStrategy = expireStrategy;
        this.executorService = executorService;
    }

    @Override
    public Integer getMaximumSize() {
        return maximumSize;
    }

    @Override
    public Duration getExpireDuration() {
        return expireDuration;
    }

    @Override
    public ExpireStrategy getExpireStrategy() {
        return expireStrategy;
    }

    @Override
    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setMaximumSize(Integer maximumSize) {
        this.maximumSize = maximumSize;
    }

    public void setExpireDuration(Duration expireDuration) {
        this.expireDuration = expireDuration;
    }

    public void setExpireStrategy(ExpireStrategy expireStrategy) {
        this.expireStrategy = expireStrategy;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    @Override
    public String toString() {
        return "CaffeineConfParams{" +
                "maximumSize=" + maximumSize +
                ", expireDuration=" + expireDuration +
                ", expireStrategy=" + expireStrategy +
                ", executorService=" + executorService +
                '}';
    }

}
