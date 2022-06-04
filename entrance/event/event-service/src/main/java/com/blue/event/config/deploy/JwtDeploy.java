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

    private transient Long globalMaxExpireMillis;

    private transient Long globalMinExpireMillis;

    private transient Long globalRefreshExpireMillis;

    private Integer localCacheCapacity;

    private Long localExpireMillis;

    private Integer refresherCorePoolSize;

    private Integer refresherMaximumPoolSize;

    private Long refresherKeepAliveTime;

    private Integer refresherBlockingQueueCapacity;

    private transient String signKey;

    private transient List<String> gammaSecrets;

    public JwtDeploy() {
    }

    public Long getGlobalMaxExpireMillis() {
        return globalMaxExpireMillis;
    }

    public void setGlobalMaxExpireMillis(Long globalMaxExpireMillis) {
        this.globalMaxExpireMillis = globalMaxExpireMillis;
    }

    public Long getGlobalMinExpireMillis() {
        return globalMinExpireMillis;
    }

    public void setGlobalMinExpireMillis(Long globalMinExpireMillis) {
        this.globalMinExpireMillis = globalMinExpireMillis;
    }

    public Long getGlobalRefreshExpireMillis() {
        return globalRefreshExpireMillis;
    }

    public void setGlobalRefreshExpireMillis(Long globalRefreshExpireMillis) {
        this.globalRefreshExpireMillis = globalRefreshExpireMillis;
    }

    public Integer getLocalCacheCapacity() {
        return localCacheCapacity;
    }

    public void setLocalCacheCapacity(Integer localCacheCapacity) {
        this.localCacheCapacity = localCacheCapacity;
    }

    public Long getLocalExpireMillis() {
        return localExpireMillis;
    }

    public void setLocalExpireMillis(Long localExpireMillis) {
        this.localExpireMillis = localExpireMillis;
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
                "globalMaxExpireMillis=" + globalMaxExpireMillis +
                ", globalMinExpireMillis=" + globalMinExpireMillis +
                ", globalRefreshExpireMillis=" + globalRefreshExpireMillis +
                ", localCacheCapacity=" + localCacheCapacity +
                ", localExpireMillis=" + localExpireMillis +
                ", refresherCorePoolSize=" + refresherCorePoolSize +
                ", refresherMaximumPoolSize=" + refresherMaximumPoolSize +
                ", refresherKeepAliveTime=" + refresherKeepAliveTime +
                ", refresherBlockingQueueCapacity=" + refresherBlockingQueueCapacity +
                ", signKey='" + "" + '\'' +
                ", gammaSecrets=" + "" +
                '}';
    }

}
