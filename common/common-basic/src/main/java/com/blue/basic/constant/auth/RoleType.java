package com.blue.basic.constant.auth;


/**
 * role type
 *
 * @author liuyunfei
 */
public enum RoleType {

    /**
     * rest for client
     */
    API(1, "role for client"),

    /**
     * rest for manager
     */
    MANAGE(2, "role for manager"),

    /**
     * role for open api
     */
    OPEN(3, "role for open api");

    public final int identity;

    public final String disc;

    RoleType(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}
