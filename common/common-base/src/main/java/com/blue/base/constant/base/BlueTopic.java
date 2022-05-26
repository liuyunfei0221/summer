package com.blue.base.constant.base;

/**
 * pulsar topics
 *
 * @author liuyunfei
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
    ACCESS_EXPIRE("accessExpire"),

    /**
     * invalid local access
     */
    INVALID_LOCAL_ACCESS("invalidLocalAccess"),

    /**
     * invalid global auth
     */
    INVALID_AUTH("invalidAuth"),

    /**
     * System authority infos Refresh
     */
    SYSTEM_AUTHORITY_INFOS_REFRESH("systemAuthorityInfosRefresh"),

    /**
     * Region infos invalid
     */
    REGION_INFOS_INVALID("regionInfosInvalid"),

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
