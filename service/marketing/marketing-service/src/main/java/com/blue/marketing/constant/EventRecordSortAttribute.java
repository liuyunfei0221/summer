package com.blue.marketing.constant;

/**
 * event record sort columns
 *
 * @author liuyunfei
 */
public enum EventRecordSortAttribute {

    /**
     * id
     */
    ID("id", "id"),

    /**
     * createTime
     */
    CREATE_TIME("createTime", "create_time");

    public final String attribute;

    public final String column;

    EventRecordSortAttribute(String attribute, String column) {
        this.attribute = attribute;
        this.column = column;
    }

}
