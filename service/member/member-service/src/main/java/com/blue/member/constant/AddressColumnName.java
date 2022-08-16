package com.blue.member.constant;

/**
 * column names of address
 *
 * @author liuyunfei
 */
public enum AddressColumnName {

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
     * create time
     */
    CREATE_TIME("createTime"),

    /**
     * update time
     */
    UPDATE_TIME("updateTime");

    public final String name;

    AddressColumnName(String name) {
        this.name = name;
    }

}
