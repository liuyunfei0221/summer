package com.blue.verify.config.deploy;

import com.blue.base.constant.base.RandomType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * verify deploy
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "verify")
public class VerifyDeploy {

    private Integer keyLength;

    private RandomType randomType;

    private Integer verifyLength;

    private Integer minLength;

    private Integer maxLength;

    private Integer expireMillis;

    private Boolean repeatable;

    public VerifyDeploy() {
    }

    public Integer getKeyLength() {
        return keyLength;
    }

    public void setKeyLength(Integer keyLength) {
        this.keyLength = keyLength;
    }

    public RandomType getRandomType() {
        return randomType;
    }

    public void setRandomType(RandomType randomType) {
        this.randomType = randomType;
    }

    public Integer getVerifyLength() {
        return verifyLength;
    }

    public void setVerifyLength(Integer verifyLength) {
        this.verifyLength = verifyLength;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Integer getExpireMillis() {
        return expireMillis;
    }

    public void setExpireMillis(Integer expireMillis) {
        this.expireMillis = expireMillis;
    }

    public Boolean getRepeatable() {
        return repeatable;
    }

    public void setRepeatable(Boolean repeatable) {
        this.repeatable = repeatable;
    }

    @Override
    public String toString() {
        return "VerifyDeploy{" +
                "keyLength=" + keyLength +
                ", randomType=" + randomType +
                ", verifyLength=" + verifyLength +
                ", minLength=" + minLength +
                ", maxLength=" + maxLength +
                ", expireMillis=" + expireMillis +
                ", repeatable=" + repeatable +
                '}';
    }

}
