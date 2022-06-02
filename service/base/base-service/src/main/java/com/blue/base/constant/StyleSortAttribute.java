package com.blue.base.constant;

/**
 * style sort columns
 *
 * @author liuyunfei
 */
public enum StyleSortAttribute {

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

    StyleSortAttribute(String attribute, String column) {
        this.attribute = attribute;
        this.column = column;
    }

}
