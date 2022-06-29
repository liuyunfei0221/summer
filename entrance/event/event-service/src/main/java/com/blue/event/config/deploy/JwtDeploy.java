package com.blue.event.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * jwt deploy
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtDeploy {

    private transient Long globalMaxExpiresMillis;

    private transient Long globalMinExpiresMillis;

    private transient Long globalRefreshExpiresMillis;

    private Integer localCacheCapacity;

    private Long localExpiresMillis;

    private Integer refresherCorePoolSize;

    private Integer refresherMaximumPoolSize;

    private Long refresherKeepAliveTime;

    private Integer refresherBlockingQueueCapacity;

    private transient String signKey;

    private transient List<String> gammaSecrets;

    public JwtDeploy() {
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

    public Long getRefresherKeepAliveTime() {
        return refresherKeepAliveTime;
    }

    public void setRefresherKeepAliveTime(Long refresherKeepAliveTime) {
        this.refresherKeepAliveTime = refresherKeepAliveTime;
    }

    public Integer getRefresherBlockingQueueCapacity() {
        return refresherBlockingQueueCapacity;
    }

    public void setRefresherBlockingQueueCapacity(Integer refresherBlockingQueueCapacity) {
        this.refresherBlockingQueueCapacity = refresherBlockingQueueCapacity;
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
        return "JwtDeploy{" +
                "globalMaxExpiresMillis=" + globalMaxExpiresMillis +
                ", globalMinExpiresMillis=" + globalMinExpiresMillis +
                ", globalRefreshExpiresMillis=" + globalRefreshExpiresMillis +
                ", localCacheCapacity=" + localCacheCapacity +
                ", localExpiresMillis=" + localExpiresMillis +
                ", refresherCorePoolSize=" + refresherCorePoolSize +
                ", refresherMaximumPoolSize=" + refresherMaximumPoolSize +
                ", refresherKeepAliveTime=" + refresherKeepAliveTime +
                ", refresherBlockingQueueCapacity=" + refresherBlockingQueueCapacity +
                ", signKey='" + "" + '\'' +
                ", gammaSecrets=" + "" +
                '}';
    }

}
