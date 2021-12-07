package com.blue.base.constant.secure;


/**
 * login type
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "SpellCheckingInspection", "GrazieInspection"})
public enum LoginType {

    /**
     * login with phone num and message verify
     */
    SMS_VERIFY("SV", "CLI", "login with phone num and message verify"),

    /**
     * login with phone num and password
     */
    PHONE_PWD("PP", "CLI", "login with phone num and password"),

    /**
     * login with email and password
     */
    EMAIL_PWD("EP", "CLI", "login with email and password"),

    /**
     * login by wechat
     */
    WECHAT("WE", "CLI", "login by wechat"),

    /**
     * login by wechar mini pro
     */
    MINI_PRO("MP", "MP", "login by wechar mini pro"),

    /**
     * not login
     */
    NOT_LOGGED_IN("NLI", "CLI", "not login");

    /**
     * identity
     */
    public final String identity;

    /**
     * nature
     */
    public final String nature;

    /**
     * disc
     */
    public final String disc;

    LoginType(String identity, String nature, String disc) {
        this.identity = identity;
        this.nature = nature;
        this.disc = disc;
    }
}
