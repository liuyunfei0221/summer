package com.blue.basic.constant.common;

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
     * Refresh rewards
     */
    REWARDS_REFRESH("refreshRewards"),

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
