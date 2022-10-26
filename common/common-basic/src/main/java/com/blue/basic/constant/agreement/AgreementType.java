package com.blue.basic.constant.agreement;

/**
 * agreement types
 *
 * @author liuyunfei
 */
public enum AgreementType {

    //agreement type 1.PLATFORM 2.SERVICE 3.PRIVACY 4.MEMBER 5.BUSINESS 6.EXCLUSION_CLAUSE

    /**
     * platform
     */
    PLATFORM(1, "platform"),

    /**
     * service
     */
    SERVICE(2, "service"),

    /**
     * privacy
     */
    PRIVACY(3, "privacy"),

    /**
     * member
     */
    MEMBER(4, "member"),

    /**
     * business
     */
    BUSINESS(5, "business"),

    /**
     * EXCLUSION_CLAUSE
     */
    EXCLUSION_CLAUSE(6, "exclusion clause");


    /**
     * identity
     */
    public final int identity;

    /**
     * disc
     */
    public final String disc;

    AgreementType(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }
}
