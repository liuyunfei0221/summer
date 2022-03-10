package com.blue.base.constant.verify;

/**
 * verify business type
 *
 * @author liuyunfei
 * @date 2021/12/23
 * @apiNote
 */
public enum BusinessType {

    /**
     * register
     */
    REGISTER("REG"),

    /**
     * sms verify login with auto register
     */
    PHONE_VERIFY_LOGIN("PVL"),

    /**
     * sms verify login with auto register
     */
    PHONE_VERIFY_LOGIN_WITH_AUTO_REGISTER("PVL_WAR"),

    /**
     * email verify login with auto register
     */
    EMAIL_VERIFY_LOGIN_WITH_AUTO_REGISTER("EVL_WAR"),

    /**
     * reset password
     */
    RESET_PWD("RST_PWD");

    public final String identity;

    BusinessType(String identity) {
        this.identity = identity;
    }
}
