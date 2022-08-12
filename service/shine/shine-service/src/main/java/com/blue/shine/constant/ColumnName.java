package com.blue.shine.constant;

/**
 * column names of shine modules
 *
 * @author liuyunfei
 */
public enum ColumnName {

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
     * addressDetail
     */
    ADDRESS_DETAIL("addressDetail"),

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
