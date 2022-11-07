package com.blue.basic.constant.risk;


/**
 * hit risk type
 *
 * @author liuyunfei
 */
public enum RiskType {

    /**
     * Single interface access is too frequent
     */
    SINGLE_INTERFACE_ACCESS_TOO_FREQUENT(1, 1, "Single interface access is too frequent"),

    /**
     * Multi interfaces access is too frequent
     */
    MULTI_INTERFACES_ACCESS_TOO_FREQUENT(2, 2, "Multi interfaces access is too frequent"),

    /**
     * Sensitive interfaces access is too frequent
     */
    SENSITIVE_INTERFACES_ACCESS_TOO_FREQUENT(3, 3, "Sensitive interfaces access is too frequent"),

    /**
     * Invalid access is too frequent
     */
    INVALID_ACCESS_TOO_FREQUENT(4, 4, "Invalid access is too frequent"),

    /**
     * Hit a specific risk control rule
     */
    HIT_A_SPECIFIC_RISK_CONTROL_RULE(5, 5, "Hit a specific risk control rule");

    public final int identity;

    public final int precedence;

    public final String disc;

    RiskType(int identity, int precedence, String disc) {
        this.identity = identity;
        this.precedence = precedence;
        this.disc = disc;
    }

}
