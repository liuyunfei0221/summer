package com.blue.lake.config.deploy;

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

    private int accessMaximumSize;

    private int sessionMaximumSize;

    private int expiresSecond;

    public CaffeineDeploy() {
    }

    public int getAccessMaximumSize() {
        return accessMaximumSize;
    }

    public void setAccessMaximumSize(int accessMaximumSize) {
        this.accessMaximumSize = accessMaximumSize;
    }

    public int getSessionMaximumSize() {
        return sessionMaximumSize;
    }

    public void setSessionMaximumSize(int sessionMaximumSize) {
        this.sessionMaximumSize = sessionMaximumSize;
    }

    public int getExpiresSecond() {
        return expiresSecond;
    }

    public void setExpiresSecond(int expiresSecond) {
        this.expiresSecond = expiresSecond;
    }

    @Override
    public String toString() {
        return "CaffeineDeploy{" +
                "accessMaximumSize=" + accessMaximumSize +
                ", sessionMaximumSize=" + sessionMaximumSize +
                ", expiresSecond=" + expiresSecond +
                '}';
    }

}
