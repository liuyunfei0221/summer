package com.blue.verify.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * cors deploy
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "verify")
public class VerifyDeploy {

    private Integer verifyLength;

    private Integer minLength;

    private Integer maxLength;

    private Integer expireMillis;

    private Boolean repeatable;

    public VerifyDeploy() {
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
                ", verifyLength=" + verifyLength +
                ", minLength=" + minLength +
                ", maxLength=" + maxLength +
                ", expireMillis=" + expireMillis +
                ", repeatable=" + repeatable +
                '}';
    }

}
