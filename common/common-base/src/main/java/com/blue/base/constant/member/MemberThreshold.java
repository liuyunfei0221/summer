package com.blue.base.constant.member;

/**
 * member attr threshold
 *
 * @author liuyunfei
 */
public enum MemberThreshold {

    /**
     * min of birth year
     */
    MIN_YEAR_OF_BIRTH(1970),

    /**
     * min of birth month
     */
    MIN_MONTH_OF_BIRTH(1),

    /**
     * max of birth month
     */
    MAX_MONTH_OF_BIRTH(12),

    /**
     * min of birth month
     */
    MIN_DAY_OF_BIRTH(1),

    /**
     * max of birth month
     */
    MAX_DAY_OF_BIRTH(31);

    public final int threshold;

    MemberThreshold(int threshold) {
        this.threshold = threshold;
    }

}
