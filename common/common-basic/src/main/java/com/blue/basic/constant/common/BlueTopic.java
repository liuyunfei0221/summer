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
    VERIFY_MESSAGE("verify-message"),

    /**
     * Illegal ip or jwt prefix mark
     */
    ILLEGAL_MARK("illegal-mark"),

    /**
     * Data reporting channel/pre-reporting and post-reporting are common
     */
    REQUEST_EVENT("request-event"),

    /**
     * Risk strategy update
     */
    RISK_STRATEGY_UPDATE("risk-strategy-update"),

    /**
     * invalid local access
     */
    INVALID_LOCAL_ACCESS("invalid-local-access"),

    /**
     * invalid global auth
     */
    INVALID_AUTH("invalid-auth"),

    /**
     * System authority infos Refresh
     */
    SYSTEM_AUTHORITY_INFOS_REFRESH("system-authority-infos-refresh"),

    /**
     * finance flow
     */
    FINANCE_FLOW("finance-flow"),

    /**
     * order summary insert
     */
    ORDER_SUMMARY_INSERT("order-summary-insert"),

    /**
     * order summary update
     */
    ORDER_SUMMARY_UPDATE("order-summary-update"),

    /**
     * Region infos invalid
     */
    REGION_INFOS_INVALID("region-infos-invalid"),

    /**
     * Refresh the sign-in data expiration time
     */
    SIGN_EXPIRE("sign-expire"),

    /**
     * Refresh rewards
     */
    REWARDS_REFRESH("refresh-rewards"),

    /**
     * Marketing/Promotional Events
     */
    MARKETING("marketing"),

    /**
     * shine insert event
     */
    SHINE_INSERT("shine-insert"),

    /**
     * shine update event
     */
    SHINE_UPDATE("shine-update"),

    /**
     * shine delete event
     */
    SHINE_DELETE("shine-delete");

    /**
     * topic name
     */
    public final String name;

    BlueTopic(String name) {
        this.name = name;
    }

}
