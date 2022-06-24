package com.blue.auth.constant;

/**
 * credential history sort columns
 *
 * @author liuyunfei
 */
public enum CredentialHistorySortAttribute {

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

    CredentialHistorySortAttribute(String attribute, String column) {
        this.attribute = attribute;
        this.column = column;
    }

}
