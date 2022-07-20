package com.blue.marketing.constant;

/**
 * reward sort columns
 *
 * @author liuyunfei
 */
public enum RewardSortAttribute {

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

    RewardSortAttribute(String attribute, String column) {
        this.attribute = attribute;
        this.column = column;
    }

}
