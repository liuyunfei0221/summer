package com.blue.auth.constant;

/**
 * resource sort columns
 *
 * @author liuyunfei
 */
public enum ResourceSortAttribute {

    /**
     * id
     */
    ID("id", "id"),

    /**
     * createTime
     */
    CREATE_TIME("createTime", "create_time");

    public final String attribute;

    public final String column;

    ResourceSortAttribute(String attribute, String column) {
        this.attribute = attribute;
        this.column = column;
    }
    
}
