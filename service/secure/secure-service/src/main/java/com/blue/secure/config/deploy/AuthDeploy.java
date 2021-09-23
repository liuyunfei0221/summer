package com.blue.secure.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * auth配置参数类
 *
 * @author DarkBlue
 */
@SuppressWarnings("SpellCheckingInspection")
@Component
@ConfigurationProperties(prefix = "auth")
public class AuthDeploy {

    private Long globalMaxExpireMillis;

    private Long globalMinExpireMillis;

    private Integer localCacherCapacity;

    private Long localExpireMillis;

    private Integer refresherCorePoolSize;

    private Integer refresherMaximumPoolSize;

    private Long refresherKeepAliveTime;

    private Integer refresherBlockingQueueCapacity;

    private Integer batchExpireMaxPerHandle;

    private Integer batchExpireScheduledCorePoolSize;

    private Long batchExpireScheduledInitialDelayMillis;

    private Long batchExpireScheduledDelayMillis;

    private Integer batchExpireQueueCapacity;

    private String signKey;

    private List<String> gammaSecrets;

    private String issuer;

    private String subject;

    private String audience;

    public AuthDeploy() {
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

    public Integer getLocalCacherCapacity() {
        return localCacherCapacity;
    }

    public void setLocalCacherCapacity(Integer localCacherCapacity) {
        this.localCacherCapacity = localCacherCapacity;
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

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getAudience() {
        return audience;
    }

    public void setAudience(String audience) {
        this.audience = audience;
    }

    @Override
    public String toString() {
        return "AuthDeploy{" +
                "globalMaxExpireMillis=" + globalMaxExpireMillis +
                ", globalMinExpireMillis=" + globalMinExpireMillis +
                ", localCacherCapacity=" + localCacherCapacity +
                ", localExpireMillis=" + localExpireMillis +
                ", refresherCorePoolSize=" + refresherCorePoolSize +
                ", refresherMaximumPoolSize=" + refresherMaximumPoolSize +
                ", refresherKeepAliveTime=" + refresherKeepAliveTime +
                ", refresherBlockingQueueCapacity=" + refresherBlockingQueueCapacity +
                ", batchExpireMaxPerHandle=" + batchExpireMaxPerHandle +
                ", batchExpireScheduledCorePoolSize=" + batchExpireScheduledCorePoolSize +
                ", batchExpireScheduledInitialDelayMillis=" + batchExpireScheduledInitialDelayMillis +
                ", batchExpireScheduledDelayMillis=" + batchExpireScheduledDelayMillis +
                ", batchExpireQueueCapacity=" + batchExpireQueueCapacity +
                ", signKey='" + signKey + '\'' +
                ", gammaSecrets=" + gammaSecrets +
                ", issuer='" + issuer + '\'' +
                ", subject='" + subject + '\'' +
                ", audience='" + audience + '\'' +
                '}';
    }

}
