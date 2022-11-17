package com.blue.risk.constant;

/**
 * risk strategy sort columns
 *
 * @author liuyunfei
 */
public enum RiskStrategySortAttribute {

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

    RiskStrategySortAttribute(String attribute, String column) {
        this.attribute = attribute;
        this.column = column;
    }

}
