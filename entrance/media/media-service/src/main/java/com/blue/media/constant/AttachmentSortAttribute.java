package com.blue.media.constant;

/**
 * attachment sort columns
 *
 * @author liuyunfei
 */
public enum AttachmentSortAttribute {

    /**
     * id
     */
    ID("id", "id"),

    /**
     * size
     */
    SIZE("size", "size"),

    /**
     * createTime
     */
    CREATE_TIME("createTime", "createTime");

    public final String attribute;

    public final String column;

    AttachmentSortAttribute(String attribute, String column) {
        this.attribute = attribute;
        this.column = column;
    }

}