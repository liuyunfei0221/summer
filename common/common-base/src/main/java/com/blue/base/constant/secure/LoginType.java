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
    SMS_VERIFY("SV", 1, "CLI", "login with phone num and message verify"),

    /**
     * login with phone num and password
     */
    PHONE_PWD("PP", 2, "CLI", "login with phone num and password"),

    /**
     * login with email and password
     */
    EMAIL_PWD("EP", 3, "CLI", "login with email and password"),

    /**
     * login by wechat
     */
    WECHAT("WE", 4, "CLI", "login by wechat"),

    /**
     * login by wechar mini pro
     */
    MINI_PRO("MP", 5, "MP", "login by wechar mini pro"),

    /**
     * not login
     */
    NOT_LOGGED_IN("NLI", 0, "CLI", "not login");

    /**
     * identity
     */
    public final String identity;


    /**
     * type
     */
    public final int type;

    /**
     * nature
     */
    public final String nature;

    /**
     * disc
     */
    public final String disc;

    LoginType(String identity, int type, String nature, String disc) {
        this.identity = identity;
        this.type = type;
        this.nature = nature;
        this.disc = disc;
    }
}
