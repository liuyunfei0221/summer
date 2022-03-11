package com.blue.auth.constant;

/**
 * role sort columns
 *
 * @author liuyunfei
 * @date 2021/10/21
 * @apiNote
 */
public enum RoleSortAttribute {

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

    RoleSortAttribute(String attribute, String column) {
        this.attribute = attribute;
        this.column = column;
    }
    
}
