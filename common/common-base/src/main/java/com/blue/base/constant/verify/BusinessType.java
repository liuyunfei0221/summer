package com.blue.base.constant.verify;

/**
 * verify business type
 *
 * @author liuyunfei
 */
public enum BusinessType {

    /**
     * register
     */
    REGISTER("REG"),

    /**
     * setting up credential
     */
    CREDENTIAL_SETTING_UP("CSU"),

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
     * update access/password
     */
    UPDATE_ACCESS("UPD_ACC"),

    /**
     * reset access/password
     */
    RESET_ACCESS("RST_ACC");

    public final String identity;

    BusinessType(String identity) {
        this.identity = identity;
    }
}
