package com.blue.basic.constant.auth;


/**
 * credential type
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "SpellCheckingInspection", "GrazieInspection"})
public enum CredentialType {

    /**
     * session with phone num and message verify with auto register
     */
    PHONE_VERIFY_AUTO_REGISTER("PVAR", "CLI", false, "APP", "session with phone num and message verify"),

    /**
     * session with phone num and password
     */
    PHONE_PWD("PP", "CLI", true, "APP", "session with phone num and password"),

    /**
     * session by local phone num with auto register
     */
    LOCAL_PHONE_AUTO_REGISTER("LPAR", "CLI", false, "APP", "session with local phone no"),

    /**
     * session with email address and message verify with auto register
     */
    EMAIL_VERIFY_AUTO_REGISTER("EVAR", "CLI", false, "APP", "session with email address and message verify"),

    /**
     * session with email and password
     */
    EMAIL_PWD("EP", "CLI", true, "APP", "session with email and password"),

    /**
     * session by wechat
     */
    WECHAT_AUTO_REGISTER("WEAR", "CLI", false, "WE", "session by wechat"),

    /**
     * session by wechar mini pro with auto register
     */
    MINI_PRO_AUTO_REGISTER("MPAR", "MPAR", false, "WE", "session by wechar mini pro"),

    /**
     * not session
     */
    NOT_LOGGED_IN("NLI", "CLI", false, "NONE", "not session");

    /**
     * identity
     */
    public final String identity;

    /**
     * nature
     */
    public final String nature;

    /**
     * allow img turing/pwd?
     */
    public final boolean allowTuring;

    /**
     * source
     */
    public final String source;

    /**
     * disc
     */
    public final String disc;

    CredentialType(String identity, String nature, boolean allowTuring, String source, String disc) {
        this.identity = identity;
        this.nature = nature;
        this.allowTuring = allowTuring;
        this.source = source;
        this.disc = disc;
    }
}
