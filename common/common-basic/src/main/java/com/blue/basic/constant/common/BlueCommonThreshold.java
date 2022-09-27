package com.blue.basic.constant.common;

/**
 * threshold values or default values
 *
 * @author liuyunfei
 */
public enum BlueCommonThreshold {

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
    MAX_ROWS(50L),

    /**
     * min count per page
     */
    MIN_COUNT(0L),

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
     * phone number/email length minimum for member session
     */
    ID_LEN_MIN(8L),

    /**
     * phone number/email length maximum for member session
     */
    ID_LEN_MAX(64L),

    /**
     * system default id
     */
    BLUE_ID(0L),

    /**
     * account number minimum
     */
    ACCOUNT_LEN_MIN(9L),

    /**
     * account number maximum
     */
    ACCOUNT_LEN_MAX(22L),

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
    ACS_LEN_MAX(128L),

    /**
     * verify value minimum
     */
    VF_K_LEN_MIN(4L),

    /**
     * verify value maximum
     */
    VF_K_LEN_MAX(128L),

    /**
     * verify value minimum
     */
    VF_V_LEN_MIN(4L),

    /**
     * verify value maximum
     */
    VF_V_LEN_MAX(128L),

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
     * member id for not session
     */
    NOT_LOGGED_IN_MEMBER_ID(0L),

    /**
     * member role id for not session
     */
    NOT_LOGGED_IN_ROLE_ID(0L),

    /**
     * member role id for unknown
     */
    UNKNOWN_LOGGED_IN_ROLE_ID(-1L),

    /**
     * session timestamp for not session
     */
    NOT_LOGGED_IN_TIME(0L),

    /**
     * min of year
     */
    MIN_YEAR(1970L),

    /**
     * min of month
     */
    MIN_MONTH(1L),

    /**
     * max of month
     */
    MAX_MONTH(12L),

    /**
     * min day of month
     */
    MIN_DAY_OF_MONTH(1L),

    /**
     * max day of month
     */
    MAX_DAY_OF_MONTH(31L),

    /**
     * min of title
     */
    TITLE_LEN_MIN(1L),

    /**
     * max of title
     */
    TITLE_LEN_MAX(128L),

    /**
     * min of content
     */
    CONTENT_LEN_MIN(1L),

    /**
     * max of content
     */
    CONTENT_LEN_MAX(512L),

    /**
     * min of detail
     */
    DETAIL_LEN_MIN(1L),

    /**
     * max of detail
     */
    DETAIL_LEN_MAX(512L),

    /**
     * min of link
     */
    LINK_LEN_MIN(1L),

    /**
     * max of link
     */
    LINK_LEN_MAX(128L);

    /**
     * number
     */
    public final long value;

    BlueCommonThreshold(long value) {
        this.value = value;
    }

}
