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
    CITY_ID("cityId");

    public final String name;

    ColumnName(String name) {
        this.name = name;
    }

}
