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
    QUESTION_MARK("?"),

    /**
     * database url params concatenation
     */
    HYPHEN("-"),

    /**
     * special params concatenation
     */
    PAR_CONCATENATION_SPEC("&&BLUE&&"),

    /**
     * wildcard
     */
    ASTERISK("*"),

    /**
     * db wildcard
     */
    PERCENT("%"),

    /**
     * element separator
     */
    COMMA(","),

    /**
     * path separator
     */
    SLASH("/"),

    /**
     * schema separator
     */
    PERIOD("."),

    /**
     * key-value separator
     */
    COLON(":"),

    /**
     * pair separator
     */
    SEMICOLON(";"),

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
