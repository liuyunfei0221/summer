package com.blue.base.constant.common;

/**
 * is default?
 *
 * @author liuyunfei
 */
public enum Default {

    /**
     * default
     */
    DEFAULT(true, "default"),

    /**
     * not default
     */
    NOT_DEFAULT(false, "not default");

    /**
     * identity
     */
    public final boolean status;

    /**
     * disc
     */
    public final String disc;

    Default(boolean status, String disc) {
        this.status = status;
        this.disc = disc;
    }
}
