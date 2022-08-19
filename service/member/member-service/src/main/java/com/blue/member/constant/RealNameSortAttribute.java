package com.blue.member.constant;

/**
 * real name sort columns
 *
 * @author liuyunfei
 */
public enum RealNameSortAttribute {

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

    RealNameSortAttribute(String attribute, String column) {
        this.attribute = attribute;
        this.column = column;
    }

}
