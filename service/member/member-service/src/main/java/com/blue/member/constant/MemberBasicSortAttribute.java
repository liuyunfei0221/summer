package com.blue.member.constant;

/**
 * member basic sort columns
 *
 * @author liuyunfei
 */
public enum MemberBasicSortAttribute {

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

    MemberBasicSortAttribute(String attribute, String column) {
        this.attribute = attribute;
        this.column = column;
    }

}
