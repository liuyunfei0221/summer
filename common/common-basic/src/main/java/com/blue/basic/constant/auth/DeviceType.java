package com.blue.basic.constant.auth;

/**
 * device type
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public enum DeviceType {

    /**
     * desk top
     */
    DESK_TOP("D", "desk top"),

    /**
     * mobile
     */
    MOBILE("M", "mobile"),

    /**
     * tablet
     */
    TABLET("T", "tablet"),

    /**
     * other devices
     */
    OTHER("O", "other devices"),

    /**
     * unknown device
     */
    UNKNOWN("U", "unknown device");

    /**
     * identity
     */
    public final String identity;

    /**
     * disc
     */
    public final String disc;

    DeviceType(String identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}
