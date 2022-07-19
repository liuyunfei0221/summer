package com.blue.basic.constant.common;

/**
 * http schemas
 *
 * @author liuyunfei
 */
public enum HttpSchema {

    /**
     * http
     */
    HTTP("http"),

    /**
     * https
     */
    HTTPS("https");

    /**
     * schema
     */
    public final String schema;

    HttpSchema(String schema) {
        this.schema = schema;
    }

}
