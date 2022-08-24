package com.blue.verify.constant;

/**
 * verify history sort columns
 *
 * @author liuyunfei
 */
public enum VerifyHistorySortAttribute {

    /**
     * id
     */
    ID("id", "id"),

    /**
     * createTime
     */
    CREATE_TIME("createTime", "createTime");

    public final String attribute;

    public final String column;

    VerifyHistorySortAttribute(String attribute, String column) {
        this.attribute = attribute;
        this.column = column;
    }

}
