package com.blue.finance.constant;

/**
 * finance flow sort columns
 *
 * @author liuyunfei
 */
public enum FinanceFlowSortAttribute {

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

    FinanceFlowSortAttribute(String attribute, String column) {
        this.attribute = attribute;
        this.column = column;
    }

}
