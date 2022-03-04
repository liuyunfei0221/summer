package com.blue.member.constant;

/**
 * member basic sort columns
 *
 * @author liuyunfei
 * @date 2021/10/21
 * @apiNote
 */
public enum MemberBasicSortAttribute {

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

    MemberBasicSortAttribute(String attribute, String column) {
        this.attribute = attribute;
        this.column = column;
    }

}
