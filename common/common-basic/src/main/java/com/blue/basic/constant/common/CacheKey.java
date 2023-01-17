package com.blue.basic.constant.common;

/**
 * cache key
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public enum CacheKey {

    /**
     * all resource key
     */
    RESOURCES("RESOURCES"),

    /**
     * all roles key
     */
    ROLES("ROLES"),

    /**
     * all relations key
     */
    ROLE_RES_RELS("ROLE_RES_RELS"),

    /**
     * default role
     */
    DEFAULT_ROLE("SUMMER_DEFAULT_ROLE");

    public final String key;

    CacheKey(String key) {
        this.key = key;
    }

}
