package com.blue.base.constant.base;

/**
 * common path variable
 *
 * @author liuyunfei
 * @date 2021/11/9
 * @apiNote
 */
public enum PathVariable {

    /**
     * id
     */
    ID("id"),

    /**
     * type
     */
    TYPE("type");

    public final String key;

    PathVariable(String key) {
        this.key = key;
    }

}
