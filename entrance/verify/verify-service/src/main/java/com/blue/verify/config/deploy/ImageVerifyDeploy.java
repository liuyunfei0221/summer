package com.blue.verify.config.deploy;

import com.blue.base.constant.base.RandomType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * image verify deploy
 *
 * @author liuyunfei
 */
@SuppressWarnings("SpellCheckingInspection")
@Component
@ConfigurationProperties(prefix = "imagev")
public class ImageVerifyDeploy {

    private Integer keyLength;

    private RandomType keyRandomType;

    private String imageType;

    private Integer verifyLength;

    private Integer expireMillis;

    private Integer allow;

    private Long sendIntervalMillis;

    public ImageVerifyDeploy() {
    }

    public Integer getKeyLength() {
        return keyLength;
    }

    public void setKeyLength(Integer keyLength) {
        this.keyLength = keyLength;
    }

    public RandomType getKeyRandomType() {
        return keyRandomType;
    }

    public void setKeyRandomType(RandomType keyRandomType) {
        this.keyRandomType = keyRandomType;
    }

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
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
        return "ImageVerifyDeploy{" +
                "keyLength=" + keyLength +
                ", keyRandomType=" + keyRandomType +
                ", imageType='" + imageType + '\'' +
                ", verifyLength=" + verifyLength +
                ", expireMillis=" + expireMillis +
                ", allow=" + allow +
                ", sendIntervalMillis=" + sendIntervalMillis +
                '}';
    }

}
