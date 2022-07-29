package com.blue.basic.constant.finance;


/**
 * refund type
 *
 * @author liuyunfei
 */
public enum RefundType {

    /**
     * withdraw
     */
    WITHDRAW(1, "withdraw");

    public final int identity;

    public final String disc;

    RefundType(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}
