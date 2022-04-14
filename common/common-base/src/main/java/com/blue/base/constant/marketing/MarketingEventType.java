package com.blue.base.constant.marketing;

/**
 * marketing event type
 *
 * @author liuyunfei
 */
public enum MarketingEventType {

    /**
     * reward for sign in
     */
    SIGN_IN_REWARD(1, "reward for sign in"),

    /**
     * reward for participate in activity
     */
    ACTIVITY_REWARD(2, "reward for participate in activity"),

    /**
     * unknown
     */
    UNKNOWN(-1, "unknown");

    /**
     * identity
     */
    public final int identity;

    /**
     * disc
     */
    public final String disc;

    MarketingEventType(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}
