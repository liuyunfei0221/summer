package com.blue.media.constant;

/**
 * column names of attachment
 *
 * @author liuyunfei
 */
public enum AttachmentColumnName {

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

    AttachmentColumnName(String name) {
        this.name = name;
    }

}
