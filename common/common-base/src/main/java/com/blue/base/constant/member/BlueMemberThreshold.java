package com.blue.base.constant.member;

/**
 * threshold values or default values
 *
 * @author liuyunfei
 */
public enum BlueMemberThreshold {

    /**
     * name min len
     */
    NAME_LEN_MIN(2L),

    /**
     * name max len
     */
    NAME_LEN_MAX(64L),

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
     * min of birth year
     */
    MIN_YEAR_OF_BIRTH(1970L),

    /**
     * min of birth month
     */
    MIN_MONTH_OF_BIRTH(1L),

    /**
     * max of birth month
     */
    MAX_MONTH_OF_BIRTH(12L),

    /**
     * min of birth month
     */
    MIN_DAY_OF_BIRTH(1L),

    /**
     * max of birth month
     */
    MAX_DAY_OF_BIRTH(31L),

    /**
     * min height
     */
    MIN_HEIGHT(30L),

    /**
     * max height
     */
    MAX_HEIGHT(300L),

    /**
     * min weight
     */
    MIN_WEIGHT(20L),

    /**
     * max weight
     */
    MAX_WEIGHT(300L),

    /**
     * contact min length
     */
    CONTACT_LEN_MIN(3L),

    /**
     * contact max length
     */
    CONTACT_LEN_MAX(64L),

    /**
     * address detail min length
     */
    ADDR_DETAIL_LEN_MIN(4L),

    /**
     * address detail max length
     */
    ADDR_DETAIL_LEN_MAX(64L),

    /**
     * reference min length
     */
    REFERENCE_LEN_MIN(4L),

    /**
     * reference max length
     */
    REFERENCE_LEN_MAX(64L),

    /**
     * extra min length
     */
    EXTRA_LEN_MIN(4L),

    /**
     * extra max length
     */
    EXTRA_LEN_MAX(64L);

    /**
     * number
     */
    public final long value;

    BlueMemberThreshold(long value) {
        this.value = value;
    }

}
