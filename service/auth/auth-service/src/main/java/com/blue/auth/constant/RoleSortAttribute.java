package com.blue.auth.constant;

/**
 * role sort columns
 *
 * @author liuyunfei
 */
public enum RoleSortAttribute {

    /**
     * id
     */
    ID("id", "id"),

    /**
     * level
     */
    LEVEL("level", "level"),

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

    RoleSortAttribute(String attribute, String column) {
        this.attribute = attribute;
        this.column = column;
    }

}
