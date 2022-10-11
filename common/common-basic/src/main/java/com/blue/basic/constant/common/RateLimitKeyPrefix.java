package com.blue.basic.constant.common;

/**
 * rate limit key prefix
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public enum RateLimitKeyPrefix {

    /**
     * IMAGE_VERIFY_RATE_LIMIT_KEY_PRE
     */
    IMAGE_VERIFY_RATE_LIMIT_KEY_PRE("image_v_"),

    /**
     * SMS_VERIFY_RATE_LIMIT_KEY_PRE
     */
    SMS_VERIFY_RATE_LIMIT_KEY_PRE("sms_v_"),

    /**
     * MAIL_VERIFY_RATE_LIMIT_KEY_PRE
     */
    MAIL_VERIFY_RATE_LIMIT_KEY_PRE("mail_v_"),

    /**
     * TURING_TEST_LIMIT_KEY_PRE
     */
    TURING_TEST_LIMIT_KEY_PRE("turing_test_"),

    /**
     * ACCESS_UPDATE_RATE_LIMIT_KEY_PRE
     */
    ACCESS_UPDATE_RATE_LIMIT_KEY_PRE("acc_up_"),

    /**
     * CREDENTIAL_SETTING_UP_RATE_LIMIT_KEY_PRE
     */
    CREDENTIAL_SETTING_UP_RATE_LIMIT_KEY_PRE("cre_su_"),

    /**
     * CREDENTIAL_UPDATE_RATE_LIMIT_KEY_PRE
     */
    CREDENTIAL_UPDATE_RATE_LIMIT_KEY_PRE("cre_up_");

    public final String prefix;

    RateLimitKeyPrefix(String prefix) {
        this.prefix = prefix;
    }

}
