package com.blue.media.constant;

/**
 * column names of attachment,download history
 *
 * @author liuyunfei
 */
public enum ColumnName {

    /**
     * id
     */
    ID("id"),

    /**
     * link
     */
    LINK("link"),

    /**
     * name
     */
    NAME("name"),

    /**
     * create time
     */
    CREATE_TIME("createTime");

    public final String name;

    ColumnName(String name) {
        this.name = name;
    }

}
