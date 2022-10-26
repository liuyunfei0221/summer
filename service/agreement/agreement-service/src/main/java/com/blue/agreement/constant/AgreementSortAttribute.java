package com.blue.agreement.constant;

/**
 * agreement sort columns
 *
 * @author liuyunfei
 */
public enum AgreementSortAttribute {

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

    AgreementSortAttribute(String attribute, String column) {
        this.attribute = attribute;
        this.column = column;
    }

}
