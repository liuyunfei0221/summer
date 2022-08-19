package com.blue.auth.constant;

/**
 * resource sort columns
 *
 * @author liuyunfei
 */
public enum ResourceSortAttribute {

    /**
     * createTime
     */
    CREATE_TIME("createTime", "create_time"),

    /**
     * updateTime
     */
    UPDATE_TIME("updateTime", "update_time");

    public final String attribute;

    public final String column;

    ResourceSortAttribute(String attribute, String column) {
        this.attribute = attribute;
        this.column = column;
    }

}
