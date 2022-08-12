package com.blue.member.constant;

/**
 * card sort columns
 *
 * @author liuyunfei
 */
public enum CardSortAttribute {

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

    CardSortAttribute(String attribute, String column) {
        this.attribute = attribute;
        this.column = column;
    }

}
