package com.blue.base.constant.base;

/**
 * element/data status
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public enum Status {

    /**
     * VALID
     */
    VALID(1, "valid"),
    /**
     * INVALID
     */
    INVALID(0, "invalid");

    /**
     * status
     */
    public final int status;

    /**
     * disc
     */
    public final String disc;

    Status(int status, String disc) {
        this.status = status;
        this.disc = disc;
    }

}
