package com.blue.member.constant;

/**
 * column names of card
 *
 * @author liuyunfei
 */
public enum CardColumnName {

    /**
     * name
     */
    NAME("name"),

    /**
     * detail
     */
    DETAIL("detail"),

    /**
     * coverLink
     */
    COVER_LINK("coverLink"),

    /**
     * contentLink
     */
    CONTENT_LINK("contentLink"),

    /**
     * create time
     */
    CREATE_TIME("createTime"),

    /**
     * update time
     */
    UPDATE_TIME("updateTime");

    public final String name;

    CardColumnName(String name) {
        this.name = name;
    }

}
