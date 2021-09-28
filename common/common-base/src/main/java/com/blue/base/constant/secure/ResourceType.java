package com.blue.base.constant.secure;


/**
 * resource type
 *
 * @author DarkBlue
 */
public enum ResourceType {

    /**
     * rest for client
     */
    FRONT(1, "rest for client"),

    /**
     * rest for manager
     */
    MANAGE(2, "rest for manager"),

    /**
     * open api
     */
    API(3, "open api");

    public final int identity;

    public final String disc;

    ResourceType(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}
