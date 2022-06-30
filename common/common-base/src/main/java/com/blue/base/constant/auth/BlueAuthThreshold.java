package com.blue.base.constant.auth;

/**
 * threshold values or auth values
 *
 * @author liuyunfei
 */
public enum BlueAuthThreshold {

    /**
     * phone number minimum
     */
    PHONE_LEN_MIN(11L),

    /**
     * phone number maximum
     */
    PHONE_LEN_MAX(18L),

    /**
     * email length minimum
     */
    EMAIL_LEN_MIN(11L),

    /**
     * email length maximum
     */
    EMAIL_LEN_MAX(64L),

    /**
     * credential length minimum
     */
    CREDENTIAL_LEN_MIN(13L),

    /**
     * credential length maximum
     */
    CREDENTIAL_LEN_MAX(64L),

    /**
     * password minimum
     */
    ACS_LEN_MIN(8L),

    /**
     * password maximum
     */
    ACS_LEN_MAX(256L),

    /**
     * verify minimum
     */
    VFC_LEN_MIN(4L),

    /**
     * verify maximum
     */
    VFC_LEN_MAX(256L),

    /**
     * security question minimum
     */
    SEC_QUESTION_LEN_MIN(8L),

    /**
     * security question maximum
     */
    SEC_QUESTION_LEN_MAX(256L),

    /**
     * security answer minimum
     */
    SEC_ANSWER_LEN_MIN(8L),

    /**
     * security answer maximum
     */
    SEC_ANSWER_LEN_MAX(256L),

    /**
     * member id for not login
     */
    NOT_LOGGED_IN_MEMBER_ID(0L),

    /**
     * member role id for not login
     */
    NOT_LOGGED_IN_ROLE_ID(0L),

    /**
     * member role id for unknown
     */
    UNKNOWN_LOGGED_IN_ROLE_ID(-1L),

    /**
     * login timestamp for not login
     */
    NOT_LOGGED_IN_TIME(0L);

    /**
     * number
     */
    public final long value;

    BlueAuthThreshold(long value) {
        this.value = value;
    }

}
