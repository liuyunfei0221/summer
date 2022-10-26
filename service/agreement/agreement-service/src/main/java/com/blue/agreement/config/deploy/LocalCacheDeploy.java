package com.blue.agreement.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.temporal.ChronoUnit;

/**
 * local cache deploy
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "local-cache")
public class LocalCacheDeploy {

    private int size;

    private int expireTime;

    private static final ChronoUnit UNIT = ChronoUnit.SECONDS;

    public LocalCacheDeploy() {
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(int expireTime) {
        this.expireTime = expireTime;
    }

    public ChronoUnit getUnit() {
        return UNIT;
    }
}
