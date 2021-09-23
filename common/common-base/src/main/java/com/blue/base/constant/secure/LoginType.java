package com.blue.base.constant.secure;


/**
 * 登陆类型
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public enum LoginType {

    /**
     * 短信验证码登录
     */
    SMS_VERIGY("SV", "CLI", "手机号加短信验证码登录"),

    /**
     * 邮箱密码登录
     */
    PHONE_PWD("PP", "CLI", "手机号加密码登录"),

    /**
     * 邮箱密码登录
     */
    EMAIL_PWD("EP", "CLI", "邮箱加密码登录"),

    /**
     * 微信登录
     */
    WECHAT("WE", "CLI", "微信登录"),

    /**
     * 小程序登录
     */
    MINI_PRO("MP", "MP", "小程序登录"),

    /**
     * 未登录
     */
    NOT_LOGGED_IN("NLI", "CLI", "未登录");

    /**
     * 类型的数字标识
     */
    public final String identity;

    /**
     * nature
     */
    public final String nature;

    /**
     * 文字描述
     */
    public final String disc;

    LoginType(String identity, String nature, String disc) {
        this.identity = identity;
        this.nature = nature;
        this.disc = disc;
    }
}
