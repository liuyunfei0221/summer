package com.blue.verify.config.deploy;

import com.blue.base.constant.base.RandomType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * image verify deploy
 *
 * @author DarkBlue
 */
@SuppressWarnings("SpellCheckingInspection")
@Component
@ConfigurationProperties(prefix = "imagev")
public class ImageVerifyDeploy {

    private Integer keyLength;

    private RandomType keyRandomType;

    private Integer verifyLength;

    private Integer expireMillis;

    private String imageType;

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

    public String getImageType() {
        return imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    @Override
    public String toString() {
        return "ImageVerifyDeploy{" +
                "keyLength=" + keyLength +
                ", keyRandomType=" + keyRandomType +
                ", verifyLength=" + verifyLength +
                ", expireMillis=" + expireMillis +
                ", imageType='" + imageType + '\'' +
                '}';
    }

}
