package com.blue.base.constant.base;

/**
 * element/data status
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public enum BlueBoolean {

    /**
     * TRUE
     */
    TRUE(1, true, "true"),
    /**
     * FALSE
     */
    FALSE(0, false, "false");

    /**
     * status
     */
    public final int status;

    /**
     * boolean
     */
    public final boolean bool;

    /**
     * disc
     */
    public final String disc;

    BlueBoolean(int status, boolean bool, String disc) {
        this.status = status;
        this.bool = bool;
        this.disc = disc;
    }
}
