package com.blue.basic.constant.finance;


/**
 * payment type
 *
 * @author liuyunfei
 */
public enum PaymentType {

    /**
     * withdraw
     */
    WITHDRAW(1, "withdraw");

    public final int identity;

    public final String disc;

    PaymentType(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}
