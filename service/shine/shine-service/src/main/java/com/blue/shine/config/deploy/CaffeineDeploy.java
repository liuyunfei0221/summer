package com.blue.shine.config.deploy;

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

    private int cityMaximumSize;

    private int expiresSecond;

    public CaffeineDeploy() {
    }

    public int getCityMaximumSize() {
        return cityMaximumSize;
    }

    public void setCityMaximumSize(int cityMaximumSize) {
        this.cityMaximumSize = cityMaximumSize;
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
                "cityMaximumSize=" + cityMaximumSize +
                ", expiresSecond=" + expiresSecond +
                '}';
    }

}
