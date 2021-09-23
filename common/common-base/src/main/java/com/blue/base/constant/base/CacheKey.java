package com.blue.base.constant.base;

/**
 * 缓存key前缀
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public enum CacheKey {

    /**
     * 用户SESSION_KEY前缀
     */
    SESSION_KEY_PRE("B_SN:"),

    /**
     * 认证信息动态刷新key前缀
     */
    AUTH_REFRESH_KEY_PRE("AUTH_REF:"),

    /**
     * 验证码key前缀
     */
    VERIFY_KEY_PRE("VERIFY_PRE:"),

    /**
     * 签到KEY前缀
     */
    SIGN_IN_KEY_PRE("SI_N:"),

    //缓存key
    /**
     * 首页公告缓存KEY前缀
     */
    PORTALS_PRE("PORTALS_PRE:");

    public final String key;

    CacheKey(String key) {
        this.key = key;
    }

}
