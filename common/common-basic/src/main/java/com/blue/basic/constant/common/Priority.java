package com.blue.basic.constant.common;

/**
 * priority
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public enum Priority {

    /**
     * default
     */
    DEFAULT(1, "default"),

    /**
     * max
     */
    MAX(0, "max"),

    /**
     * min
     */
    MIN(10000, "min");

    /**
     * value
     */
    public final int value;

    /**
     * disc
     */
    public final String disc;

    Priority(int value, String disc) {
        this.value = value;
        this.disc = disc;
    }

}
