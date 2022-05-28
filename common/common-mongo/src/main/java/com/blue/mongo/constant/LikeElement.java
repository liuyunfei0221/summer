package com.blue.mongo.constant;

/**
 * elements for like
 *
 * @author liuyunfei
 */
public enum LikeElement {

    /**
     * PREFIX
     */
    PREFIX("^.*"),

    /**
     * SUFFIX
     */
    SUFFIX(".*$");

    public final String element;

    LikeElement(String element) {
        this.element = element;
    }

}
