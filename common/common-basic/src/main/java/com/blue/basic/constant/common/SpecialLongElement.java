package com.blue.basic.constant.common;

/**
 * Special long
 *
 * @author liuyunfei
 */
public enum SpecialLongElement {

    /**
     * zero value
     */
    ZERO(0L),

    /**
     * one value
     */
    ONE(1L),

    /**
     * duration before millis to second
     */
    MILLIS_SEC_DURATION(1000L);

    /**
     * value
     */
    public final long value;

    SpecialLongElement(long value) {
        this.value = value;
    }
}
