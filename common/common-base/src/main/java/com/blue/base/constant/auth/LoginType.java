package com.blue.base.constant.auth;


/**
 * login type
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "SpellCheckingInspection", "GrazieInspection"})
public enum LoginType {

    /**
     * login with phone num and message verify with auto register
     */
    PHONE_VERIFY_AUTO_REGISTER("PVAR", "CLI", "login with phone num and message verify"),

    /**
     * login with phone num and password
     */
    PHONE_PWD("PP", "CLI", "login with phone num and password"),

    /**
     * login with email address and message verify with auto register
     */
    EMAIL_VERIFY_AUTO_REGISTER("EVAR", "CLI", "login with email address and message verify"),

    /**
     * login with email and password
     */
    EMAIL_PWD("EP", "CLI", "login with email and password"),

    /**
     * login by wechat
     */
    WECHAT_AUTO_REGISTER("WEAR", "CLI", "login by wechat"),

    /**
     * login by wechar mini pro with auto register
     */
    MINI_PRO_AUTO_REGISTER("MPAR", "MPAR", "login by wechar mini pro"),

    /**
     * login by local phone num with auto register
     */
    LOCAL_PHONE_AUTO_REGISTER("LPAR", "CLI", ""),

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
