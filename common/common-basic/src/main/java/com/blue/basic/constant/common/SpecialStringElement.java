package com.blue.basic.constant.common;

/**
 * Special string
 *
 * @author liuyunfei
 */
public enum SpecialStringElement {

    /**
     * empty value
     */
    EMPTY_VALUE(""),

    /**
     * empty json
     */
    EMPTY_JSON("{}"),

    /**
     * privacy value
     */
    PRIVACY_VALUE(":)");

    /**
     * value
     */
    public final String value;

    SpecialStringElement(String value) {
        this.value = value;
    }
}
