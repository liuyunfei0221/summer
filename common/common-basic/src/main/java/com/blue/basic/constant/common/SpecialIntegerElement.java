package com.blue.basic.constant.common;

/**
 * Special long
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public enum SpecialIntegerElement {

    /**
     * zero value
     */
    ZERO(0),

    /**
     * one value
     */
    ONE(1),

    /**
     * Minus one
     */
    MINUS_ONE(-1);

    /**
     * value
     */
    public final int value;

    SpecialIntegerElement(int value) {
        this.value = value;
    }
}
