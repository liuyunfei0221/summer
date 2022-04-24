package com.blue.member.constant;

/**
 * member business sort columns
 *
 * @author liuyunfei
 */
public enum MemberBusinessSortAttribute {

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

    MemberBusinessSortAttribute(String attribute, String column) {
        this.attribute = attribute;
        this.column = column;
    }

}
