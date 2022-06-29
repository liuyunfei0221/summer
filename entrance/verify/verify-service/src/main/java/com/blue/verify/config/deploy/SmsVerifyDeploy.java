package com.blue.verify.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * image verify deploy
 *
 * @author liuyunfei
 */
@SuppressWarnings("SpellCheckingInspection")
@Component
@ConfigurationProperties(prefix = "smsv")
public class SmsVerifyDeploy {

    private Integer verifyLength;

    private Long expiresMillis;

    private Integer allow;

    private Long sendIntervalMillis;

    public SmsVerifyDeploy() {
    }

    public Integer getVerifyLength() {
        return verifyLength;
    }

    public void setVerifyLength(Integer verifyLength) {
        this.verifyLength = verifyLength;
    }

    public Long getExpiresMillis() {
        return expiresMillis;
    }

    public void setExpiresMillis(Long expiresMillis) {
        this.expiresMillis = expiresMillis;
    }

    public Integer getAllow() {
        return allow;
    }

    public void setAllow(Integer allow) {
        this.allow = allow;
    }

    public Long getSendIntervalMillis() {
        return sendIntervalMillis;
    }

    public void setSendIntervalMillis(Long sendIntervalMillis) {
        this.sendIntervalMillis = sendIntervalMillis;
    }

    @Override
    public String toString() {
        return "SmsVerifyDeploy{" +
                "verifyLength=" + verifyLength +
                ", expiresMillis=" + expiresMillis +
                ", allow=" + allow +
                ", sendIntervalMillis=" + sendIntervalMillis +
                '}';
    }

}
