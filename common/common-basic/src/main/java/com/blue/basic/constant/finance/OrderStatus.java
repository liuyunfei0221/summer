package com.blue.basic.constant.finance;


/**
 * order status
 *
 * @author liuyunfei
 */
public enum OrderStatus {

    /**
     * paying
     */
    PAYING(1, "paying"),

    /**
     * paid
     */
    PAID(2, "paid"),

    /**
     * unpaid
     */
    UNPAID(3, "unpaid"),

    /**
     * complete
     */
    COMPLETE(4, "complete"),

    /**
     * cancel
     */
    CANCEL(5, "cancel");

    public final int identity;

    public final String disc;

    OrderStatus(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}
