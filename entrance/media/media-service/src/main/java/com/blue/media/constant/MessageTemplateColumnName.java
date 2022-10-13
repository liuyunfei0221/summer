package com.blue.media.constant;

/**
 * column names of message template
 *
 * @author liuyunfei
 */
public enum MessageTemplateColumnName {

    /**
     * name
     */
    NAME("name"),

    /**
     * title
     */
    TITLE("title"),

    /**
     * create time
     */
    CREATE_TIME("createTime"),

    /**
     * update time
     */
    UPDATE_TIME("updateTime");

    public final String name;

    MessageTemplateColumnName(String name) {
        this.name = name;
    }

}
