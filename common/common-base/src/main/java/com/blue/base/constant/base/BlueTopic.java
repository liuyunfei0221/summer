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
    ILLEGAL_MARK("illegalMark"),

    /**
     * Data reporting channel/pre-reporting and post-reporting are common
     */
    REQUEST_EVENT("requestEvent"),

    /**
     * Refresh the certification expiration time
     */
    AUTH_EXPIRE("authExpire"),

    /**
     * Local auth certification expired
     */
    INVALID_LOCAL_AUTH("invalidLocalAuth"),

    /**
     * System authority Infos Refresh
     */
    SYSTEM_AUTHORITY_INFOS_REFRESH("systemAuthorityInfosRefresh"),

    /**
     * Refresh the sign-in data expiration time
     */
    SIGN_EXPIRE("signExpire"),

    /**
     * Marketing/Promotional Events
     */
    MARKETING("marketing");


    /**
     * topic name
     */
    public final String name;

    BlueTopic(String name) {
        this.name = name;
    }

}
