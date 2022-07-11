package com.blue.base.constant.finance;


/**
 * flow type
 *
 * @author liuyunfei
 */
public enum FlowType {

    /**
     * withdraw
     */
    WITHDRAW(1, "withdraw");

    public final int identity;

    public final String disc;

    FlowType(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}
