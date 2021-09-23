package com.blue.base.constant.base;

/**
 * 特殊字符配置
 *
 * @author DarkBlue
 */
public enum SecKey {

    /**
     * 未登录特殊secKey
     */
    NOT_LOGGED_IN_SEC_KEY("", "未登录特殊secKey");

    /**
     * 数值
     */
    public final String value;

    /**
     * 描述
     */
    public final String disc;

    SecKey(String value, String disc) {
        this.value = value;
        this.disc = disc;
    }
}
