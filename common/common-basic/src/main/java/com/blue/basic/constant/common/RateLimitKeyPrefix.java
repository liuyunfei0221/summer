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
    IMAGE_VERIFY_RATE_LIMIT_KEY_PRE("IMAGE_V_"),

    /**
     * SMS_VERIFY_RATE_LIMIT_KEY_PRE
     */
    SMS_VERIFY_RATE_LIMIT_KEY_PRE("SMS_V_"),

    /**
     * MAIL_VERIFY_RATE_LIMIT_KEY_PRE
     */
    MAIL_VERIFY_RATE_LIMIT_KEY_PRE("MAIL_V_"),

    /**
     * VERIFIES_RATE_LIMIT_KEY_PRE
     */
    VERIFIES_RATE_LIMIT_KEY_PRE("VS_V_"),

    /**
     * ALLOW_TURING_CREDENTIAL_TYPE_RATE_LIMIT_KEY_PRE
     */
    ALLOW_TURING_CREDENTIAL_TYPE_RATE_LIMIT_KEY_PRE("AL_T_C_T_"),

    /**
     * TURING_TEST_LIMIT_KEY_PRE
     */
    TURING_TEST_LIMIT_KEY_PRE("TURING_TEST_"),

    /**
     * ACCESS_UPDATE_RATE_LIMIT_KEY_PRE
     */
    ACCESS_UPDATE_RATE_LIMIT_KEY_PRE("ACC_UP_"),

    /**
     * CREDENTIAL_SETTING_UP_RATE_LIMIT_KEY_PRE
     */
    CREDENTIAL_SETTING_UP_RATE_LIMIT_KEY_PRE("CRE_SU_"),

    /**
     * CREDENTIAL_UPDATE_RATE_LIMIT_KEY_PRE
     */
    CREDENTIAL_UPDATE_RATE_LIMIT_KEY_PRE("CRE_UP_");

    public final String prefix;

    RateLimitKeyPrefix(String prefix) {
        this.prefix = prefix;
    }

}
