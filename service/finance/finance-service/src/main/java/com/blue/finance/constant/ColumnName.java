package com.blue.finance.constant;

/**
 * column names of attachment,download history
 *
 * @author liuyunfei
 */
public enum ColumnName {

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

    ColumnName(String name) {
        this.name = name;
    }

}
