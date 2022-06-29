package com.blue.base.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * caffeine deploy
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "dict")
public class DictCaffeineDeploy {

    private int dictTypeMaximumSize;

    private int dictMaximumSize;

    private int expiresSecond;

    public DictCaffeineDeploy() {
    }

    public int getDictTypeMaximumSize() {
        return dictTypeMaximumSize;
    }

    public void setDictTypeMaximumSize(int dictTypeMaximumSize) {
        this.dictTypeMaximumSize = dictTypeMaximumSize;
    }

    public int getDictMaximumSize() {
        return dictMaximumSize;
    }

    public void setDictMaximumSize(int dictMaximumSize) {
        this.dictMaximumSize = dictMaximumSize;
    }

    public int getExpiresSecond() {
        return expiresSecond;
    }

    public void setExpiresSecond(int expiresSecond) {
        this.expiresSecond = expiresSecond;
    }

    @Override
    public String toString() {
        return "DictCaffeineDeploy{" +
                "dictTypeMaximumSize=" + dictTypeMaximumSize +
                ", dictMaximumSize=" + dictMaximumSize +
                ", expiresSecond=" + expiresSecond +
                '}';
    }

}
