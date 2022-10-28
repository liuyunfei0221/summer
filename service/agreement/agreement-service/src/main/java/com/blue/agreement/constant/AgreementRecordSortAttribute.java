package com.blue.agreement.constant;

/**
 * agreement record sort columns
 *
 * @author liuyunfei
 */
public enum AgreementRecordSortAttribute {

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

    AgreementRecordSortAttribute(String attribute, String column) {
        this.attribute = attribute;
        this.column = column;
    }

}
