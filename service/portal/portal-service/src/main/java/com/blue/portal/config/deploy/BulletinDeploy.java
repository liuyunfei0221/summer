package com.blue.portal.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * bulletin deploy
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "bulletin")
public class BulletinDeploy {

    private int maximumSize;

    private int expiresSecond;

    public BulletinDeploy() {
    }

    public int getMaximumSize() {
        return maximumSize;
    }

    public void setMaximumSize(int maximumSize) {
        this.maximumSize = maximumSize;
    }

    public int getExpiresSecond() {
        return expiresSecond;
    }

    public void setExpiresSecond(int expiresSecond) {
        this.expiresSecond = expiresSecond;
    }

    @Override
    public String toString() {
        return "BulletinDeploy{" +
                "maximumSize=" + maximumSize +
                ", expiresSecond=" + expiresSecond +
                '}';
    }

}
