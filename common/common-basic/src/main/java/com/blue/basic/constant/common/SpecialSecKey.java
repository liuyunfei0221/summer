package com.blue.basic.constant.common;

/**
 * Special sec key
 *
 * @author liuyunfei
 */
public enum SpecialSecKey {

    /**
     * special secKey is not session
     */
    NOT_LOGGED_IN_SEC_KEY("", "not access");

    /**
     * value
     */
    public final String value;

    /**
     * disc
     */
    public final String disc;

    SpecialSecKey(String value, String disc) {
        this.value = value;
        this.disc = disc;
    }
}
