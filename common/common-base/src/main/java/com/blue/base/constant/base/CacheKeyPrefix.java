package com.blue.base.constant.base;

/**
 * cache key prefix
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public enum CacheKeyPrefix {

    /**
     * illegal ips
     */
    ILLEGAL_IP_PRE("ILL_IP_P:"),

    /**
     * illegal jwts
     */
    ILLEGAL_JWT_PRE("ILL_JWT_P:"),

    /**
     * member session key prefix
     */
    SESSION_PRE("B_SN_P:"),

    /**
     * access refresh key prefix
     */
    ACCESS_REFRESH_PRE("ACC_R_P:"),

    /**
     * verify key prefix
     */
    VERIFY_PRE("VF_P:"),

    /**
     * sing in key prefix
     */
    SIGN_IN_PRE("SI_N_P:"),

    /**
     * portal key prefix
     */
    PORTALS_PRE("PTS_P:"),

    /**
     * cache manager key prefix
     */
    CACHE_MANAGER_PRE("CH_M_P:"),

    /**
     * resource cache manager key prefix
     */
    RESOURCES_PRE("RES_P:"),

    /**
     * role cache manager key prefix
     */
    ROLES_PRE("ROLE_P:"),

    /**
     * role resource relation cache manager key prefix
     */
    ROLE_RESOURCE_RELATIONS_PRE("ROLE_RES_REL_P:");

    public final String prefix;

    CacheKeyPrefix(String prefix) {
        this.prefix = prefix;
    }

}
