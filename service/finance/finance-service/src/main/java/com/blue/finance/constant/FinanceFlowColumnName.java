package com.blue.finance.constant;

/**
 * column names of finance flow
 *
 * @author liuyunfei
 */
public enum FinanceFlowColumnName {

    /**
     * id
     */
    ID("id"),

    /**
     * amount changed
     */
    AMOUNT_CHANGED("amountChanged"),

    /**
     * create time
     */
    CREATE_TIME("createTime"),

    /**
     * update time
     */
    UPDATE_TIME("updateTime");

    public final String name;

    FinanceFlowColumnName(String name) {
        this.name = name;
    }

}
