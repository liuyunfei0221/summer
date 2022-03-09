package com.blue.base.constant.verify;

/**
 * verify business type
 *
 * @author liuyunfei
 * @date 2021/12/23
 * @apiNote
 */
public enum VerifyBusinessType {

    /**
     * register
     */
    REGISTER("REGISTER"),

    /**
     * sms login with auto register
     */
    SMS_LOGIN("SMS_LOGIN_WITH_AUTO_REGISTER"),

    /**
     * sms login with auto register
     */
    SMS_LOGIN_WITH_AUTO_REGISTER("SMS_LOGIN_WITH_AUTO_REGISTER"),

    /**
     * reset password
     */
    RESET_PWD("RESET_PWD");

    public final String identity;

    VerifyBusinessType(String identity) {
        this.identity = identity;
    }
}
