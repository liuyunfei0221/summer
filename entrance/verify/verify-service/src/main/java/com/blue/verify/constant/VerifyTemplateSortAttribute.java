package com.blue.verify.constant;

/**
 * verify template sort columns
 *
 * @author liuyunfei
 */
public enum VerifyTemplateSortAttribute {

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

    VerifyTemplateSortAttribute(String attribute, String column) {
        this.attribute = attribute;
        this.column = column;
    }

}
