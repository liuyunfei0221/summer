package com.blue.risk.constant;

/**
 * column names of risk strategy
 *
 * @author liuyunfei
 */
public enum RiskStrategyColumnName {

    /**
     * name
     */
    NAME("name"),

    /**
     * description
     */
    DESCRIPTION("description"),

    /**
     * create time
     */
    CREATE_TIME("createTime"),

    /**
     * update time
     */
    UPDATE_TIME("updateTime");

    public final String name;

    RiskStrategyColumnName(String name) {
        this.name = name;
    }

}
