package com.blue.portal.constant;

/**
 * bulletin sort columns
 *
 * @author liuyunfei
 */
public enum BulletinSortAttribute {

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

    BulletinSortAttribute(String attribute, String column) {
        this.attribute = attribute;
        this.column = column;
    }

}
