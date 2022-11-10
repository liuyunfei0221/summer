package com.blue.basic.constant.analyze;


/**
 * statistics type
 *
 * @author liuyunfei
 */
public enum StatisticsType {

    /**
     * for member active
     */
    MA("MA", 1, "Single interface access is too frequent");

    public final String identity;

    public final int precedence;

    public final String disc;

    StatisticsType(String identity, int precedence, String disc) {
        this.identity = identity;
        this.precedence = precedence;
        this.disc = disc;
    }

}
