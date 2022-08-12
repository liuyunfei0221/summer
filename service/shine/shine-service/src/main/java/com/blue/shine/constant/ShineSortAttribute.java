package com.blue.shine.constant;

/**
 * shine sort columns
 *
 * @author liuyunfei
 */
public enum ShineSortAttribute {

    /**
     * id
     */
    ID("id", "id"),

    /**
     * createTime
     */
    CREATE_TIME("createTime", "createTime"),

    /**
     * updateTime
     */
    UPDATE_TIME("updateTime", "updateTime");

    public final String attribute;

    public final String column;

    ShineSortAttribute(String attribute, String column) {
        this.attribute = attribute;
        this.column = column;
    }

}
