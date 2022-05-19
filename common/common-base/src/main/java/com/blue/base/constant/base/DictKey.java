package com.blue.base.constant.base;

/**
 * dict key
 *
 * @author liuyunfei
 */
public enum DictKey {

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

    DictKey(String key) {
        this.key = key;
    }

}
