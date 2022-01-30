package com.blue.verify.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * image verify deploy
 *
 * @author DarkBlue
 */
@SuppressWarnings("SpellCheckingInspection")
@Component
@ConfigurationProperties(prefix = "smsv")
public class SmsVerifyDeploy {

    private Integer verifyLength;

    private Integer expireMillis;

    public SmsVerifyDeploy() {
    }

    public Integer getVerifyLength() {
        return verifyLength;
    }

    public void setVerifyLength(Integer verifyLength) {
        this.verifyLength = verifyLength;
    }

    public Integer getExpireMillis() {
        return expireMillis;
    }

    public void setExpireMillis(Integer expireMillis) {
        this.expireMillis = expireMillis;
    }

    @Override
    public String toString() {
        return "ImageVerifyDeploy{" +
                ", verifyLength=" + verifyLength +
                ", expireMillis=" + expireMillis +
                '}';
    }

}
