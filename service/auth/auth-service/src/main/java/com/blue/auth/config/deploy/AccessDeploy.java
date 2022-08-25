package com.blue.auth.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * access deploy
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "access")
public class AccessDeploy {

    private Integer localCacheCapacity;

    private Long globalExpiresMillis;

    private Long localExpiresMillis;

    private Long millisLeftToHandleExpire;

    private Integer refresherCorePoolSize;

    private Integer refresherMaximumPoolSize;

    private Long refresherKeepAliveSeconds;

    private Integer refresherBlockingQueueCapacity;

    private Integer batchExpireMaxPerHandle;

    private Integer batchExpireScheduledCorePoolSize;

    private Long batchExpireScheduledInitialDelayMillis;

    private Long batchExpireScheduledDelayMillis;

    private Integer batchExpireQueueCapacity;

    private Integer gammaLength;

    public AccessDeploy() {
    }

    public Integer getLocalCacheCapacity() {
        return localCacheCapacity;
    }

    public void setLocalCacheCapacity(Integer localCacheCapacity) {
        this.localCacheCapacity = localCacheCapacity;
    }

    public Long getGlobalExpiresMillis() {
        return globalExpiresMillis;
    }

    public void setGlobalExpiresMillis(Long globalExpiresMillis) {
        this.globalExpiresMillis = globalExpiresMillis;
    }

    public Long getLocalExpiresMillis() {
        return localExpiresMillis;
    }

    public void setLocalExpiresMillis(Long localExpiresMillis) {
        this.localExpiresMillis = localExpiresMillis;
    }

    public Long getMillisLeftToHandleExpire() {
        return millisLeftToHandleExpire;
    }

    public void setMillisLeftToHandleExpire(Long millisLeftToHandleExpire) {
        this.millisLeftToHandleExpire = millisLeftToHandleExpire;
    }

    public Integer getRefresherCorePoolSize() {
        return refresherCorePoolSize;
    }

    public void setRefresherCorePoolSize(Integer refresherCorePoolSize) {
        this.refresherCorePoolSize = refresherCorePoolSize;
    }

    public Integer getRefresherMaximumPoolSize() {
        return refresherMaximumPoolSize;
    }

    public void setRefresherMaximumPoolSize(Integer refresherMaximumPoolSize) {
        this.refresherMaximumPoolSize = refresherMaximumPoolSize;
    }

    public Long getRefresherKeepAliveSeconds() {
        return refresherKeepAliveSeconds;
    }

    public void setRefresherKeepAliveSeconds(Long refresherKeepAliveSeconds) {
        this.refresherKeepAliveSeconds = refresherKeepAliveSeconds;
    }

    public Integer getRefresherBlockingQueueCapacity() {
        return refresherBlockingQueueCapacity;
    }

    public void setRefresherBlockingQueueCapacity(Integer refresherBlockingQueueCapacity) {
        this.refresherBlockingQueueCapacity = refresherBlockingQueueCapacity;
    }

    public Integer getBatchExpireMaxPerHandle() {
        return batchExpireMaxPerHandle;
    }

    public void setBatchExpireMaxPerHandle(Integer batchExpireMaxPerHandle) {
        this.batchExpireMaxPerHandle = batchExpireMaxPerHandle;
    }

    public Integer getBatchExpireScheduledCorePoolSize() {
        return batchExpireScheduledCorePoolSize;
    }

    public void setBatchExpireScheduledCorePoolSize(Integer batchExpireScheduledCorePoolSize) {
        this.batchExpireScheduledCorePoolSize = batchExpireScheduledCorePoolSize;
    }

    public Long getBatchExpireScheduledInitialDelayMillis() {
        return batchExpireScheduledInitialDelayMillis;
    }

    public void setBatchExpireScheduledInitialDelayMillis(Long batchExpireScheduledInitialDelayMillis) {
        this.batchExpireScheduledInitialDelayMillis = batchExpireScheduledInitialDelayMillis;
    }

    public Long getBatchExpireScheduledDelayMillis() {
        return batchExpireScheduledDelayMillis;
    }

    public void setBatchExpireScheduledDelayMillis(Long batchExpireScheduledDelayMillis) {
        this.batchExpireScheduledDelayMillis = batchExpireScheduledDelayMillis;
    }

    public Integer getBatchExpireQueueCapacity() {
        return batchExpireQueueCapacity;
    }

    public void setBatchExpireQueueCapacity(Integer batchExpireQueueCapacity) {
        this.batchExpireQueueCapacity = batchExpireQueueCapacity;
    }

    public Integer getGammaLength() {
        return gammaLength;
    }

    public void setGammaLength(Integer gammaLength) {
        this.gammaLength = gammaLength;
    }

    @Override
    public String toString() {
        return "AccessDeploy{" +
                "localCacheCapacity=" + localCacheCapacity +
                ", globalExpiresMillis=" + globalExpiresMillis +
                ", localExpiresMillis=" + localExpiresMillis +
                ", millisLeftToHandleExpire=" + millisLeftToHandleExpire +
                ", refresherCorePoolSize=" + refresherCorePoolSize +
                ", refresherMaximumPoolSize=" + refresherMaximumPoolSize +
                ", refresherKeepAliveSeconds=" + refresherKeepAliveSeconds +
                ", refresherBlockingQueueCapacity=" + refresherBlockingQueueCapacity +
                ", batchExpireMaxPerHandle=" + batchExpireMaxPerHandle +
                ", batchExpireScheduledCorePoolSize=" + batchExpireScheduledCorePoolSize +
                ", batchExpireScheduledInitialDelayMillis=" + batchExpireScheduledInitialDelayMillis +
                ", batchExpireScheduledDelayMillis=" + batchExpireScheduledDelayMillis +
                ", batchExpireQueueCapacity=" + batchExpireQueueCapacity +
                ", gammaLength=" + gammaLength +
                '}';
    }

}
