package com.blue.base.constant.secure;


/**
 * 资源类型
 *
 * @author DarkBlue
 */
public enum ResourceType {

    /**
     * 前台接口
     */
    FRONT(1, "前台接口"),

    /**
     * 后台接口
     */
    MANAGE(2, "后台接口"),

    /**
     * 开放api
     */
    API(3, "开放api");

    public int identity;

    public String disc;

    ResourceType(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}
