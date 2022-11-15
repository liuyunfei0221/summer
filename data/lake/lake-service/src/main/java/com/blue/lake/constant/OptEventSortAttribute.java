package com.blue.lake.constant;

/**
 * opt event sort columns
 *
 * @author liuyunfei
 */
public enum OptEventSortAttribute {

    /**
     * id
     */
    ID("id", "id"),

    /**
     * cursor
     */
    CURSOR("cursor", "cursor"),

    /**
     * createTime
     */
    STAMP("stamp", "stamp");

    public final String attribute;

    public final String column;

    OptEventSortAttribute(String attribute, String column) {
        this.attribute = attribute;
        this.column = column;
    }

}
