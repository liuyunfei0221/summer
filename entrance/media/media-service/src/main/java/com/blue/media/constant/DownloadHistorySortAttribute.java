package com.blue.media.constant;

/**
 * download history sort columns
 *
 * @author liuyunfei
 */
public enum DownloadHistorySortAttribute {

    /**
     * id
     */
    ID("id", "id"),

    /**
     * createTime
     */
    CREATE_TIME("createTime", "createTime");

    public final String attribute;

    public final String column;

    DownloadHistorySortAttribute(String attribute, String column) {
        this.attribute = attribute;
        this.column = column;
    }

}