package com.blue.portal.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * caffeine deploy
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "caffeine")
public class CaffeineDeploy {

    private int maximumSize;

    private int expireSeconds;

    public CaffeineDeploy() {
    }

    public int getMaximumSize() {
        return maximumSize;
    }

    public void setMaximumSize(int maximumSize) {
        this.maximumSize = maximumSize;
    }

    public int getExpireSeconds() {
        return expireSeconds;
    }

    public void setExpireSeconds(int expireSeconds) {
        this.expireSeconds = expireSeconds;
    }

    @Override
    public String toString() {
        return "CaffeineDeploy{" +
                "maximumSize=" + maximumSize +
                ", expireSeconds=" + expireSeconds +
                '}';
    }

}
