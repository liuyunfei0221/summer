package com.blue.verify.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * risk control deploy
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "risk")
public class RiskControlDeploy {

    private Long illegalExpireSeconds;

    public RiskControlDeploy() {
    }

    public Long getIllegalExpireSeconds() {
        return illegalExpireSeconds;
    }

    public void setIllegalExpireSeconds(Long illegalExpireSeconds) {
        this.illegalExpireSeconds = illegalExpireSeconds;
    }

    @Override
    public String toString() {
        return "RiskControlDeploy{" +
                ", illegalExpireSeconds=" + illegalExpireSeconds +
                '}';
    }

}
