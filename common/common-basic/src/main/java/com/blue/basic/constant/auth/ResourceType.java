package com.blue.basic.constant.auth;


/**
 * resource type
 *
 * @author liuyunfei
 */
public enum ResourceType {

    /**
     * rest for client
     */
    API(1, "rest for client"),

    /**
     * rest for manager
     */
    MANAGE(2, "rest for manager"),

    /**
     * rest for open api
     */
    OPEN(3, "rest for open api");

    public final int identity;

    public final String disc;

    ResourceType(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}
