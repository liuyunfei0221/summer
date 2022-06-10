package com.blue.member.constant;

/**
 * column names of country,state,city,area
 *
 * @author liuyunfei
 */
public enum ColumnName {

    /**
     * id
     */
    ID("id"),

    /**
     * name
     */
    NAME("name"),

    /**
     * member name
     */
    MEMBER_NAME("memberName"),

    /**
     * phone
     */
    PHONE("phone"),

    /**
     * email
     */
    EMAIL("email"),

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
     * reference
     */
    REFERENCE("reference"),

    /**
     * create time
     */
    CREATE_TIME("createTime"),

    /**
     * update time
     */
    UPDATE_TIME("updateTime");

    public final String name;

    ColumnName(String name) {
        this.name = name;
    }

}
