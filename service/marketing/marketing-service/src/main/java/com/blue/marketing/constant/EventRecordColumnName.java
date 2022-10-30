package com.blue.marketing.constant;

/**
 * column names of event record
 *
 * @author liuyunfei
 */
public enum EventRecordColumnName {

    /**
     * create time
     */
    CREATE_TIME("createTime");

    public final String name;

    EventRecordColumnName(String name) {
        this.name = name;
    }

}
