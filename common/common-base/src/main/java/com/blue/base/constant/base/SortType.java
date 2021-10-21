package com.blue.base.constant.base;

/**
 * sort type
 *
 * @author liuyunfei
 * @date 2021/10/21
 * @apiNote
 */
public enum SortType {

    /**
     * DESC
     */
    DESC("DESC"),
    /**
     * ASC
     */
    ASC("ASC");

    /**
     * type identity
     */
    public final String identity;

    SortType(String identity) {
        this.identity = identity;
    }

}
