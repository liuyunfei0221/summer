package com.blue.base.constant.common;


/**
 * illegal request reason
 *
 * @author liuyunfei
 */
public enum IllegalReason {

    /**
     * Too Many Request
     */
    TOO_MANY_REQUEST("tooManyRequest", "Too Many Request"),

    /**
     * Too Many Ill Request
     */
    TOO_MANY_ILL_REQUEST("tooManyIllRequest", "Too Many Ill Request"),

    /**
     * unknown reason
     */
    UNKNOWN("unknown", "unknown reason");


    /**
     * reason
     */
    public final String reason;

    /**
     * disc
     */
    public final String disc;

    IllegalReason(String reason, String disc) {
        this.reason = reason;
        this.disc = disc;
    }

}
