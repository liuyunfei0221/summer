package com.blue.base.constant.common;

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
    ILLEGAL_IP_PRE("ILL_IP:"),

    /**
     * illegal jwts
     */
    ILLEGAL_JWT_PRE("ILL_JWT:"),

    /**
     * member session key prefix
     */
    SESSION_PRE("B_SN:"),

    /**
     * member id key prefix
     */
    MEMBER_PRE("MB:"),

    /**
     * verify key prefix
     */
    VERIFY_PRE("VF:"),

    /**
     * sing in key prefix
     */
    SIGN_IN_PRE("SI_N:"),

    /**
     * bulletins key prefix
     */
    BULLETINS_PRE("BTS:"),

    /**
     * style key prefix
     */
    ACTIVE_STYLE_PRE("AST:"),

    /**
     * cache manager key prefix
     */
    CACHE_MANAGER_PRE("CH_M:"),

    /**
     * resource cache manager key prefix
     */
    RESOURCES_PRE("RES:"),

    /**
     * role cache manager key prefix
     */
    ROLES_PRE("ROLE:"),

    /**
     * role resource relation cache manager key prefix
     */
    ROLE_RESOURCE_RELATIONS_PRE("ROLE_RES_REL:");

    public final String prefix;

    CacheKeyPrefix(String prefix) {
        this.prefix = prefix;
    }

}
