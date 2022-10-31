package com.blue.basic.constant.finance;


/**
 * order article status
 *
 * @author liuyunfei
 */
public enum OrderArticleStatus {

    /**
     * to be confirmed
     */
    TO_BE_CONFIRMED(1, "to be confirmed"),

    /**
     * performing
     */
    PERFORMING(2, "performing"),

    /**
     * performed
     */
    PERFORMED(3, "performing"),

    /**
     * cancel
     */
    CANCEL(4, "cancel");

    public final int identity;

    public final String disc;

    OrderArticleStatus(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}
