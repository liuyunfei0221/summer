package com.blue.base.constant.base;

/**
 * pulsar topics
 *
 * @author DarkBlue
 */
public enum BlueTopic {

    /**
     * Illegal ip or jwt prefix mark
     */
    ILLEGAL_MARK("illegalMark", "Illegal ip or jwt prefix mark"),

    /**
     * Data reporting channel/pre-reporting and post-reporting are common
     */
    REQUEST_EVENT("requestEvent", "Data reporting channel/pre-reporting and post-reporting are common"),

    /**
     * Refresh the certification expiration time
     */
    AUTH_EXPIRE("authExpire", "Refresh the certification expiration time"),

    /**
     * Local auth certification expired
     */
    INVALID_LOCAL_AUTH("invalidLocalAuth", "Local auth certification expired"),

    /**
     * Refresh the sign-in data expiration time
     */
    SIGN_EXPIRE("signExpire", "Refresh the sign-in data expiration time"),

    /**
     * Marketing/Promotional Events
     */
    MARKETING("marketing", "Marketing/Promotional Events");


    /**
     * topic名称
     */
    public final String name;

    /**
     * 描述
     */
    public final String disc;

    BlueTopic(String name, String disc) {
        this.name = name;
        this.disc = disc;
    }

}
