package com.blue.verify.constant;

/**
 * column names of verify history
 *
 * @author liuyunfei
 */
public enum ColumnName {

    /**
     * id
     */
    ID("id"),

    /**
     * create time
     */
    CREATE_TIME("createTime");

    public final String name;

    ColumnName(String name) {
        this.name = name;
    }

}
