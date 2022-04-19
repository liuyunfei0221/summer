package com.blue.base.constant.marketing;

/**
 * reward type
 *
 * @author liuyunfei
 */
public enum RewardType {

    /**
     * VIRTUAL_GOOD
     */
    VIRTUAL_GOOD(1, "virtual good");

    public final int identity;

    public final String disc;

    RewardType(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }
}
