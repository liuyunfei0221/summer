package com.blue.base.constant.finance;


/**
 * flow change type
 *
 * @author liuyunfei
 */
public enum FlowChangeType {

    /**
     * income
     */
    INCOME(1, "income"),

    /**
     * outlay
     */
    OUTLAY(2, "outlay"),

    /**
     * freeze
     */
    FREEZE(3, "freeze"),

    /**
     * unfreeze
     */
    UN_FREEZE(4, "unfreeze"),

    /**
     * meaning less
     */
    MEANINGLESS(5, "meaning less");

    public final int identity;

    public final String disc;

    FlowChangeType(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}
