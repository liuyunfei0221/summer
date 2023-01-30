package com.blue.media.constant;

/**
 * column names of qr code config
 *
 * @author liuyunfei
 */
public enum QrCodeConfigColumnName {

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

    QrCodeConfigColumnName(String name) {
        this.name = name;
    }

}