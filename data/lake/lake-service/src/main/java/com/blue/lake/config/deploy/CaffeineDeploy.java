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

    private int expiresSecond;

    public CaffeineDeploy() {
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
                "expiresSecond=" + expiresSecond +
                '}';
    }

}
