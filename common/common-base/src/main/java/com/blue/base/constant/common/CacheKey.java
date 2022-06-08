package com.blue.base.constant.common;

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
    RESOURCES("RESOURCES_A"),

    /**
     * all roles key
     */
    ROLES("ROLES_A"),

    /**
     * default role
     */
    DEFAULT_ROLE("SUMMER_DEFAULT_ROLE"),

    /**
     * all relations key
     */
    ROLE_RES_RELS("ROLE_RES_RELS");

    public final String key;

    CacheKey(String key) {
        this.key = key;
    }

}
