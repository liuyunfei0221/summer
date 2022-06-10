package com.blue.member.constant;

/**
 * address sort columns
 *
 * @author liuyunfei
 */
public enum AddressSortAttribute {

    /**
     * id
     */
    ID("id", "id"),

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

    AddressSortAttribute(String attribute, String column) {
        this.attribute = attribute;
        this.column = column;
    }

}
