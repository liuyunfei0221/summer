package com.blue.base.constant.base;

/**
 * Special sec key
 *
 * @author DarkBlue
 */
public enum SpecialSecKey {

    /**
     * special secKey is not login
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
