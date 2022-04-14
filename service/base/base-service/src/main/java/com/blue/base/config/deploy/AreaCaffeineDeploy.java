package com.blue.base.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * caffeine deploy
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "area")
public class AreaCaffeineDeploy {

    private int countryMaximumSize;

    private int stateMaximumSize;

    private int cityMaximumSize;

    private int areaMaximumSize;

    private int expireSeconds;

    public AreaCaffeineDeploy() {
    }

    public int getCountryMaximumSize() {
        return countryMaximumSize;
    }

    public void setCountryMaximumSize(int countryMaximumSize) {
        this.countryMaximumSize = countryMaximumSize;
    }

    public int getStateMaximumSize() {
        return stateMaximumSize;
    }

    public void setStateMaximumSize(int stateMaximumSize) {
        this.stateMaximumSize = stateMaximumSize;
    }

    public int getCityMaximumSize() {
        return cityMaximumSize;
    }

    public void setCityMaximumSize(int cityMaximumSize) {
        this.cityMaximumSize = cityMaximumSize;
    }

    public int getAreaMaximumSize() {
        return areaMaximumSize;
    }

    public void setAreaMaximumSize(int areaMaximumSize) {
        this.areaMaximumSize = areaMaximumSize;
    }

    public int getExpireSeconds() {
        return expireSeconds;
    }

    public void setExpireSeconds(int expireSeconds) {
        this.expireSeconds = expireSeconds;
    }

    @Override
    public String toString() {
        return "AreaCaffeineDeploy{" +
                "countryMaximumSize=" + countryMaximumSize +
                ", stateMaximumSize=" + stateMaximumSize +
                ", cityMaximumSize=" + cityMaximumSize +
                ", areaMaximumSize=" + areaMaximumSize +
                ", expireSeconds=" + expireSeconds +
                '}';
    }

}
