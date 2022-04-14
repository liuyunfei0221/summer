package com.blue.base.constant.base;

/**
 * cache keys
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public enum BlueCacheKey {

    //prefix
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
    SESSION_KEY_PRE("B_SN:"),

    /**
     * access refresh key prefix
     */
    ACCESS_REFRESH_KEY_PRE("AUTH_REF:"),

    /**
     * verify key prefix
     */
    VERIFY_KEY_PRE("VERIFY_PRE:"),

    /**
     * sing in key prefix
     */
    SIGN_IN_KEY_PRE("SI_N:"),

    /**
     * portal key
     */
    PORTALS_PRE("PORTALS_PRE:");

    public final String key;

    BlueCacheKey(String key) {
        this.key = key;
    }

}
