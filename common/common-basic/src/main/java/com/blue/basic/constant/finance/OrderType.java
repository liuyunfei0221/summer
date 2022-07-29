package com.blue.basic.constant.finance;


/**
 * order type
 *
 * @author liuyunfei
 */
public enum OrderType {

    /**
     * withdraw
     */
    WITHDRAW(1, "withdraw");

    public final int identity;

    public final String disc;

    OrderType(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}
