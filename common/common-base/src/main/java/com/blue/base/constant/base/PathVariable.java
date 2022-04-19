package com.blue.base.constant.base;

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
     * pid
     */
    PID("pid"),

    /**
     * type
     */
    TYPE("type");

    public final String key;

    PathVariable(String key) {
        this.key = key;
    }

}
