package com.blue.risk.constant;

/**
 * risk hit record sort columns
 *
 * @author liuyunfei
 */
public enum RiskHitRecordSortAttribute {

    /**
     * id
     */
    ID("id", "id"),

    /**
     * createTime
     */
    STAMP("stamp", "stamp");

    public final String attribute;

    public final String column;

    RiskHitRecordSortAttribute(String attribute, String column) {
        this.attribute = attribute;
        this.column = column;
    }

}
