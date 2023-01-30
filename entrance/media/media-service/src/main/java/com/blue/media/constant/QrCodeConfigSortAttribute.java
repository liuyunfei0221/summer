package com.blue.media.constant;

/**
 * qr code config sort columns
 *
 * @author liuyunfei
 */
public enum QrCodeConfigSortAttribute {

    /**
     * id
     */
    ID("id", "id"),

    /**
     * createTime
     */
    CREATE_TIME("createTime", "create_time");

    public final String attribute;

    public final String column;

    QrCodeConfigSortAttribute(String attribute, String column) {
        this.attribute = attribute;
        this.column = column;
    }

}