package com.blue.basic.constant.common;

/**
 * common path variable
 *
 * @author liuyunfei
 */
public enum PathVariable {

    /**
     * id
     */
    ID("id"),

    /**
     * parent id
     */
    PID("pid"),

    /**
     * member id
     */
    MID("mid"),

    /**
     * type
     */
    TYPE("type");

    public final String key;

    PathVariable(String key) {
        this.key = key;
    }

}
