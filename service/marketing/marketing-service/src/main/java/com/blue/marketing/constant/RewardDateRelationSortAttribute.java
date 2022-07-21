package com.blue.marketing.constant;

/**
 * reward date relation sort columns
 *
 * @author liuyunfei
 */
public enum RewardDateRelationSortAttribute {

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

    RewardDateRelationSortAttribute(String attribute, String column) {
        this.attribute = attribute;
        this.column = column;
    }

}
