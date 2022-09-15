package com.blue.jwt.constant;

/**
 * jwt default element
 *
 * @author liuyunfei
 */
public enum JwtDefaultElement {

    /**
     * issuer
     */
    ISSUER("Blue"),

    /**
     * subject
     */
    SUBJECT("Hello"),

    /**
     * audience
     */
    AUDIENCE("Bluer");

    public final String identity;

    JwtDefaultElement(String identity) {
        this.identity = identity;
    }

}
