package com.blue.auth.config.deploy;

import com.blue.base.constant.base.RandomType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * verify config
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "verify")
public class VerifyDeploy {

    private RandomType defaultType;

    private Long defaultExpireMillis;

    private Integer defaultLength;

    public VerifyDeploy() {
    }

    public RandomType getDefaultType() {
        return defaultType;
    }

    public void setDefaultType(RandomType defaultType) {
        this.defaultType = defaultType;
    }

    public Long getDefaultExpireMillis() {
        return defaultExpireMillis;
    }

    public void setDefaultExpireMillis(Long defaultExpireMillis) {
        this.defaultExpireMillis = defaultExpireMillis;
    }

    public Integer getDefaultLength() {
        return defaultLength;
    }

    public void setDefaultLength(Integer defaultLength) {
        this.defaultLength = defaultLength;
    }

    @Override
    public String toString() {
        return "VerifyDeploy{" +
                "defaultType=" + defaultType +
                ", defaultExpireMillis=" + defaultExpireMillis +
                ", defaultLength=" + defaultLength +
                '}';
    }

}
