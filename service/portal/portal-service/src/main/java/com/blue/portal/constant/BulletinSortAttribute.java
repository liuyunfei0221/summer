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
     * priority
     */
    PRIORITY("priority", "priority"),

    /**
     * activeTime
     */
    ACTIVE_TIME("activeTime", "active_time"),

    /**
     * expireTime
     */
    EXPIRE_TIME("expireTime", "expire_time"),

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

    BulletinSortAttribute(String attribute, String column) {
        this.attribute = attribute;
        this.column = column;
    }

}
