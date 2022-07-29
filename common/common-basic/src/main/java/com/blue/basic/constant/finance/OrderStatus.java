package com.blue.basic.constant.finance;


/**
 * order type
 *
 * @author liuyunfei
 */
public enum OrderStatus {

    /**
     * withdraw
     */
    WITHDRAW(1, "withdraw");

    public final int identity;

    public final String disc;

    OrderStatus(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}
