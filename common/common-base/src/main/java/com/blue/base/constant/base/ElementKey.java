package com.blue.base.constant.base;

/**
 * element key
 *
 * @author liuyunfei
 */
public enum ElementKey {

    /**
     * default key
     */
    DEFAULT("DEFAULT"),

    /**
     * email
     */
    EMAIL("EMAIL"),

    /**
     * role
     */
    ROLE("ROLE"),

    /**
     * roles
     */
    ROLES("ROLES");

    public final String key;

    ElementKey(String key) {
        this.key = key;
    }

}
