package com.blue.basic.constant.common;

/**
 * pulsar topics
 *
 * @author liuyunfei
 */
public enum BlueTopic {

    /**
     * verify message Events
     */
    VERIFY_MESSAGE("verifyMessage"),

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
     * finance flow
     */
    FINANCE_FLOW("financeFlow"),

    /**
     * order summary insert
     */
    ORDER_SUMMARY_INSERT("orderSummaryInsert"),

    /**
     * order summary update
     */
    ORDER_SUMMARY_UPDATE("orderSummaryUpdate"),

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
    MARKETING("marketing"),

    /**
     * shine insert event
     */
    SHINE_INSERT("shineInsert"),

    /**
     * shine update event
     */
    SHINE_UPDATE("shineUpdate"),

    /**
     * shine delete event
     */
    SHINE_DELETE("shineDelete");

    /**
     * topic name
     */
    public final String name;

    BlueTopic(String name) {
        this.name = name;
    }

}
