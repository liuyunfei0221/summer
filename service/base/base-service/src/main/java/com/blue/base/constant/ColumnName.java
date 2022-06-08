package com.blue.base.constant;

/**
 * column names of country,state,city,area
 *
 * @author liuyunfei
 */
public enum ColumnName {

    /**
     * ID
     */
    ID("id"),

    /**
     * NAME
     */
    NAME("name"),

    /**
     * country id
     */
    COUNTRY_ID("countryId"),

    /**
     * state id
     */
    STATE_ID("stateId"),

    /**
     * city id
     */
    CITY_ID("cityId"),

    /**
     * native name
     */
    NATIVE_NAME("nativeName"),

    /**
     * number code
     */
    NUMERIC_CODE("numericCode"),

    /**
     * country code
     */
    COUNTRY_CODE("countryCode"),

    /**
     * phone code
     */
    PHONE_CODE("phoneCode"),

    /**
     * capital
     */
    CAPITAL("capital"),

    /**
     * top level domain
     */
    TOP_LEVEL_DOMAIN("topLevelDomain"),

    /**
     * region
     */
    REGION("region");

    public final String name;

    ColumnName(String name) {
        this.name = name;
    }

}
