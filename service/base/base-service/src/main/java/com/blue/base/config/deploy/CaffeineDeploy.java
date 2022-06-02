package com.blue.base.config.deploy;

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

    private int countryMaximumSize;

    private int stateMaximumSize;

    private int cityMaximumSize;

    private int areaMaximumSize;

    private int styleMaximumSize;

    private int expireSeconds;

    public CaffeineDeploy() {
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

    public int getStyleMaximumSize() {
        return styleMaximumSize;
    }

    public void setStyleMaximumSize(int styleMaximumSize) {
        this.styleMaximumSize = styleMaximumSize;
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
                ", styleMaximumSize=" + styleMaximumSize +
                ", expireSeconds=" + expireSeconds +
                '}';
    }

}
