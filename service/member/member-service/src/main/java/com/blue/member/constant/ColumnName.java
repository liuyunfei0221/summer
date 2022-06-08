package com.blue.member.constant;

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
     * reference
     */
    REFERENCE("reference"),

    /**
     *
     */
    CREATE_TIME("createTime"),

    /**
     * phone code
     */
    UPDATE_TIME("updateTime");

    public final String name;

    ColumnName(String name) {
        this.name = name;
    }

}
