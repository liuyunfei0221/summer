package com.blue.shine.constant;

/**
 * column names of shine entity
 *
 * @author liuyunfei
 */
public enum ShineColumnName {

    /**
     * id
     */
    ID("id"),

    /**
     * title
     */
    TITLE("title"),

    /**
     * content
     */
    CONTENT("content"),

    /**
     * detail
     */
    DETAIL("detail"),

    /**
     * contact
     */
    CONTACT("contact"),

    /**
     * contactDetail
     */
    CONTACT_DETAIL("contactDetail"),

    /**
     * countryId
     */
    COUNTRY_ID("countryId"),

    /**
     * stateId
     */
    STATE_ID("stateId"),

    /**
     * cityId
     */
    CITY_ID("cityId"),

    /**
     * addressDetail
     */
    ADDRESS_DETAIL("addressDetail"),

    /**
     * extra
     */
    EXTRA("extra"),

    /**
     * priority
     */
    PRIORITY("priority"),

    /**
     * create time
     */
    CREATE_TIME("createTime"),

    /**
     * update time
     */
    UPDATE_TIME("updateTime"),

    /**
     * creator
     */
    CREATOR("creator"),

    /**
     * updater
     */
    UPDATER("updater");

    public final String name;

    ShineColumnName(String name) {
        this.name = name;
    }

}
