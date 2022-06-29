package com.blue.auth.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * auth deploy
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "auth")
public class AuthDeploy {

    private transient Long globalMaxExpiresMillis;

    private transient Long globalMinExpiresMillis;

    private transient Long globalRefreshExpiresMillis;

    private Integer localCacheCapacity;

    private Long localExpiresMillis;

    private Integer refresherCorePoolSize;

    private Integer refresherMaximumPoolSize;

    private Long refresherKeepAliveSeconds;

    private Integer refresherBlockingQueueCapacity;

    private Integer batchExpireMaxPerHandle;

    private Integer batchExpireScheduledCorePoolSize;

    private Long batchExpireScheduledInitialDelayMillis;

    private Long batchExpireScheduledDelayMillis;

    private Integer batchExpireQueueCapacity;

    private transient String signKey;

    private transient List<String> gammaSecrets;

    public AuthDeploy() {
    }

    public Long getGlobalMaxExpiresMillis() {
        return globalMaxExpiresMillis;
    }

    public void setGlobalMaxExpiresMillis(Long globalMaxExpiresMillis) {
        this.globalMaxExpiresMillis = globalMaxExpiresMillis;
    }

    public Long getGlobalMinExpiresMillis() {
        return globalMinExpiresMillis;
    }

    public void setGlobalMinExpiresMillis(Long globalMinExpiresMillis) {
        this.globalMinExpiresMillis = globalMinExpiresMillis;
    }

    public Long getGlobalRefreshExpiresMillis() {
        return globalRefreshExpiresMillis;
    }

    public void setGlobalRefreshExpiresMillis(Long globalRefreshExpiresMillis) {
        this.globalRefreshExpiresMillis = globalRefreshExpiresMillis;
    }

    public Integer getLocalCacheCapacity() {
        return localCacheCapacity;
    }

    public void setLocalCacheCapacity(Integer localCacheCapacity) {
        this.localCacheCapacity = localCacheCapacity;
    }

    public Long getLocalExpiresMillis() {
        return localExpiresMillis;
    }

    public void setLocalExpiresMillis(Long localExpiresMillis) {
        this.localExpiresMillis = localExpiresMillis;
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

    public String getSignKey() {
        return signKey;
    }

    public void setSignKey(String signKey) {
        this.signKey = signKey;
    }

    public List<String> getGammaSecrets() {
        return gammaSecrets;
    }

    public void setGammaSecrets(List<String> gammaSecrets) {
        this.gammaSecrets = gammaSecrets;
    }

    @Override
    public String toString() {
        return "AuthDeploy{" +
                "globalMaxExpiresMillis=" + globalMaxExpiresMillis +
                ", globalMinExpiresMillis=" + globalMinExpiresMillis +
                ", globalRefreshExpiresMillis=" + globalRefreshExpiresMillis +
                ", localCacheCapacity=" + localCacheCapacity +
                ", localExpiresMillis=" + localExpiresMillis +
                ", refresherCorePoolSize=" + refresherCorePoolSize +
                ", refresherMaximumPoolSize=" + refresherMaximumPoolSize +
                ", refresherKeepAliveSeconds=" + refresherKeepAliveSeconds +
                ", refresherBlockingQueueCapacity=" + refresherBlockingQueueCapacity +
                ", batchExpireMaxPerHandle=" + batchExpireMaxPerHandle +
                ", batchExpireScheduledCorePoolSize=" + batchExpireScheduledCorePoolSize +
                ", batchExpireScheduledInitialDelayMillis=" + batchExpireScheduledInitialDelayMillis +
                ", batchExpireScheduledDelayMillis=" + batchExpireScheduledDelayMillis +
                ", batchExpireQueueCapacity=" + batchExpireQueueCapacity +
                ", signKey='" + signKey + '\'' +
                ", gammaSecrets=" + ":)" +
                '}';
    }

}
