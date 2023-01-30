package com.blue.media.constant;

/**
 * message template sort columns
 *
 * @author liuyunfei
 */
public enum MessageTemplateSortAttribute {

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

    MessageTemplateSortAttribute(String attribute, String column) {
        this.attribute = attribute;
        this.column = column;
    }

}