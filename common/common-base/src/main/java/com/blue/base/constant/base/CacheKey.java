package com.blue.base.constant.base;

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
     * all relations key
     */
    RES_ROLE_RELS("RES_ROLE_RELS_A");

    public final String key;

    CacheKey(String key) {
        this.key = key;
    }

}
