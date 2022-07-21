package com.blue.basic.constant.member;

/**
 * member attr threshold
 *
 * @author liuyunfei
 */
public enum MemberThreshold {

    /**
     * min height
     */
    MIN_HEIGHT(30),

    /**
     * max height
     */
    MAX_HEIGHT(300),

    /**
     * min weight
     */
    MIN_WEIGHT(20),

    /**
     * max weight
     */
    MAX_WEIGHT(300);

    public final int threshold;

    MemberThreshold(int threshold) {
        this.threshold = threshold;
    }

}
