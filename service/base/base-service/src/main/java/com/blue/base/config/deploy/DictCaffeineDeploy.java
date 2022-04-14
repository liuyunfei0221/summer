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

    private int expireSeconds;

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

    public int getExpireSeconds() {
        return expireSeconds;
    }

    public void setExpireSeconds(int expireSeconds) {
        this.expireSeconds = expireSeconds;
    }

    @Override
    public String toString() {
        return "DictCaffeineDeploy{" +
                "dictTypeMaximumSize=" + dictTypeMaximumSize +
                ", dictMaximumSize=" + dictMaximumSize +
                ", expireSeconds=" + expireSeconds +
                '}';
    }

}
