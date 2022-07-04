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
     * description
     */
    DESCRIPTION("description"),

    /**
     * domain
     */
    DOMAIN("domain"),

    /**
     * pathToBeFilled
     */
    PATH_TO_BE_FILLED("pathToBeFilled"),

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
