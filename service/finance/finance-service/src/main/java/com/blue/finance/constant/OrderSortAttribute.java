package com.blue.finance.constant;

/**
 * order sort columns
 *
 * @author liuyunfei
 */
public enum OrderSortAttribute {

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

    OrderSortAttribute(String attribute, String column) {
        this.attribute = attribute;
        this.column = column;
    }

}
