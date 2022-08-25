package com.blue.verify.constant;

/**
 * column names of verify template
 *
 * @author liuyunfei
 */
public enum VerifyTemplateColumnName {

    /**
     * name
     */
    NAME("name"),

    /**
     * description
     */
    DESCRIPTION("description"),

    /**
     * title
     */
    TITLE("title"),

    /**
     * content
     */
    CONTENT("content"),

    /**
     * create time
     */
    CREATE_TIME("createTime"),

    /**
     * update time
     */
    UPDATE_TIME("updateTime");

    public final String name;

    VerifyTemplateColumnName(String name) {
        this.name = name;
    }

}
