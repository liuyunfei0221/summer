package com.blue.basic.constant.finance;


/**
 * reference amount status
 *
 * @author liuyunfei
 */
public enum ReferenceAmountStatus {

    /**
     * using
     */
    USING(1, "using"),

    /**
     * used
     */
    USED(2, "used"),

    /**
     * cancel
     */
    CANCEL(3, "cancel");

    public final int identity;

    public final String disc;

    ReferenceAmountStatus(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}
