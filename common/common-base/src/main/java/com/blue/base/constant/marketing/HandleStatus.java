package com.blue.base.constant.marketing;

/**
 * handle status
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public enum HandleStatus {

    /**
     * handled
     */
    HANDLED(1, "handled"),
    /**
     * broken
     */
    BROKEN(0, "broken");

    /**
     * status
     */
    public final int status;

    /**
     * disc
     */
    public final String disc;

    HandleStatus(int status, String disc) {
        this.status = status;
        this.disc = disc;
    }

}
