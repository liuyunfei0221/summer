package com.blue.basic.constant.common;

/**
 * blue suffix
 *
 * @author liuyunfei
 */
public enum BlueSuffix {

    /**
     * properties
     */
    PROP(".properties");

    /**
     * value
     */
    public final String suffix;

    BlueSuffix(String suffix) {
        this.suffix = suffix;
    }

}
