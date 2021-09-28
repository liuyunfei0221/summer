package com.blue.base.constant.secure;

/**
 * device type
 *
 * @author DarkBlue
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
     * devices identity for not login
     */
    UNKNOWN("U", "devices identity for not login");

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
