package com.blue.basic.constant.common;

/**
 * comparison type
 *
 * @author liuyunfei
 */
public enum ComparisonType {

    /**
     * EQUALS
     */
    EQUALS("eq", "="),

    /**
     * GREATER_THAN
     */
    GREATER_THAN("gt", ">"),

    /**
     * LESS_THAN
     */
    LESS_THAN("lt", "<"),

    /**
     * GREATER_THAN_OR_EQUALS_TO
     */
    GREATER_THAN_OR_EQUALS_TO("gte", ">="),

    /**
     * LESS_THAN_OR_EQUALS_TO
     */
    LESS_THAN_OR_EQUALS_TO("lte", "<=");

    /**
     * type identity
     */
    public final String identity;

    /**
     * type expression
     */
    public final String expression;

    ComparisonType(String identity, String expression) {
        this.identity = identity;
        this.expression = expression;
    }
}
