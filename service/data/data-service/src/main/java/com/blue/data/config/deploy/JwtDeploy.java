package com.blue.data.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * jwt deploy
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtDeploy {

    private Long globalMaxExpireMillis;

    private Long globalMinExpireMillis;

    private Integer localCacherCapacity;

    private Long localExpireMillis;

    private Integer refresherCorePoolSize;

    private Integer refresherMaximumPoolSize;

    private Long refresherKeepAliveTime;

    private Integer refresherBlockingQueueCapacity;

    private String signKey;

    private List<String> gammaSecrets;

    private String issuer;

    private String subject;

    private String audience;

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
        return "JwtDeploy{" +
                "globalMaxExpireMillis=" + globalMaxExpireMillis +
                ", globalMinExpireMillis=" + globalMinExpireMillis +
                ", localCacherCapacity=" + localCacherCapacity +
                ", localExpireMillis=" + localExpireMillis +
                ", refresherCorePoolSize=" + refresherCorePoolSize +
                ", refresherMaximumPoolSize=" + refresherMaximumPoolSize +
                ", refresherKeepAliveTime=" + refresherKeepAliveTime +
                ", refresherBlockingQueueCapacity=" + refresherBlockingQueueCapacity +
                ", signKey='" + signKey + '\'' +
                ", gammaSecrets=" + gammaSecrets +
                ", issuer='" + issuer + '\'' +
                ", subject='" + subject + '\'' +
                ", audience='" + audience + '\'' +
                '}';
    }

}
