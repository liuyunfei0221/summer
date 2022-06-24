package com.blue.base.constant.common;

/**
 * symbols
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public enum Symbol {

    /**
     * request identity prefix
     */
    RATE_LIMIT_KEY_PRE("RL:"),

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
    PAIR_SEPARATOR(";");


    /**
     * identity
     */
    public final String identity;

    Symbol(String identity) {
        this.identity = identity;
    }

}
