package com.blue.basic.constant.common;

/**
 * Special string
 *
 * @author liuyunfei
 */
public enum SpecialStringElement {

    /**
     * special secKey is not session
     */
    EMPTY_DATA("");

    /**
     * value
     */
    public final String value;

    SpecialStringElement(String value) {
        this.value = value;
    }
}
