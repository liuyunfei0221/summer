package com.blue.basic.constant.common;

/**
 * symbols
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public enum Symbol {

    /**
     * unknown identity
     */
    UNKNOWN("UNKNOWN"),

    /**
     * identity
     */
    IDENTITY("id"),

    /**
     * params concatenation
     */
    PAR_CONCATENATION("_"),

    /**
     * database conf params concatenation
     */
    PAR_CONCATENATION_DATABASE_CONF("?"),

    /**
     * database url params concatenation
     */
    PAR_CONCATENATION_DATABASE_URL("-"),

    /**
     * special params concatenation
     */
    PAR_CONCATENATION_SPEC("&&BLUE&&"),

    /**
     * wildcard
     */
    WILDCARD("*"),

    /**
     * db wildcard
     */
    DATABASE_WILDCARD("%"),

    /**
     * element separator
     */
    LIST_ELEMENT_SEPARATOR(","),

    /**
     * path separator
     */
    PATH_SEPARATOR("/"),

    /**
     * schema separator
     */
    SCHEME_SEPARATOR("."),

    /**
     * url parameter separator
     */
    URL_PAR_SEPARATOR("?"),

    /**
     * key-value separator
     */
    KEY_VALUE_SEPARATOR(":"),

    /**
     * pair separator
     */
    PAIR_SEPARATOR(";"),

    /**
     * open brace
     */
    OPEN_BRACE("{"),

    /**
     * close brace
     */
    CLOSE_BRACE("}"),

    /**
     * open parenthesis
     */
    OPEN_PARENTHESIS("("),

    /**
     * close parenthesis
     */
    CLOSE_PARENTHESIS(")"),

    /**
     * open bracket
     */
    OPEN_BRACKET("["),

    /**
     * close bracket
     */
    CLOSE_BRACKET("]");

    /**
     * identity
     */
    public final String identity;

    Symbol(String identity) {
        this.identity = identity;
    }

}
