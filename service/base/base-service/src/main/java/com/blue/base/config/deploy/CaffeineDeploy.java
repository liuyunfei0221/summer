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

    private int expiresSecond;

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

    public int getExpiresSecond() {
        return expiresSecond;
    }

    public void setExpiresSecond(int expiresSecond) {
        this.expiresSecond = expiresSecond;
    }

    @Override
    public String toString() {
        return "AreaCaffeineDeploy{" +
                "countryMaximumSize=" + countryMaximumSize +
                ", stateMaximumSize=" + stateMaximumSize +
                ", cityMaximumSize=" + cityMaximumSize +
                ", areaMaximumSize=" + areaMaximumSize +
                ", expiresSecond=" + expiresSecond +
                '}';
    }

}
