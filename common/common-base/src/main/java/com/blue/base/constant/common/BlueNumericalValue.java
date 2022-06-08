package com.blue.base.constant.common;

/**
 * threshold values or default values
 *
 * @author liuyunfei
 */
public enum BlueNumericalValue {

    /**
     * default page
     */
    PAGE(1L),

    /**
     * default limit
     */
    LIMIT(0L),

    /**
     * default rows per page
     */
    ROWS(10L),

    /**
     * max rows per page
     */
    MAX_ROWS(100L),

    /**
     * polling interval millis while waiting for other threads to synchronize data
     */
    WAIT_MILLIS_FOR_THREAD_SLEEP(100L),

    /**
     * maximum waiting time of other threads during redisson synchronization
     */
    MAX_WAIT_MILLIS_FOR_REDISSON_SYNC(1000L),

    /**
     * max service queries from req
     */
    MAX_SERVICE_SELECT(500L),

    /**
     * max queries from database per request
     */
    DB_SELECT(100L),

    /**
     * max write from database per request
     */
    DB_WRITE(100L),

    /**
     * max queries from ldap per request
     */
    LDAP_SELECT(100000000L),

    /**
     * max write from ldap per request
     */
    LDAP_WRITE(10000000L),

    /**
     * phone number/email length minimum for member login
     */
    ID_LEN_MIN(8L),

    /**
     * phone number/email length maximum for member login
     */
    ID_LEN_MAX(64L),

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
     * password minimum
     */
    ACS_LEN_MIN(8L),

    /**
     * password maximum
     */
    ACS_LEN_MAX(256L),

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
    NOT_LOGGED_IN_TIME(0L),

    /**
     * maximum expiration time of monthly sign-in information(day)
     */
    MAX_EXPIRE_DAYS_FOR_SIGN(33L),

    /**
     * qr code width
     */
    QR_CODE_WIDTH(200L),

    /**
     * qr code height
     */
    QR_CODE_HEIGHT(200L),

    /**
     * qr logo width
     */
    QR_CODE_LOGO_ROUND_ARCW(20L),

    /**
     * qr logo height
     */
    QR_CODE_LOGO_ROUND_ARCH(20L),

    /**
     * random name length
     */
    RANDOM_NAME_LEN(8L),

    /**
     * system default id
     */
    BLUE_ID(0L);


    /**
     * number
     */
    public final long value;

    BlueNumericalValue(long value) {
        this.value = value;
    }

}
