package com.blue.base.constant.secure;

/**
 * 设备类型
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused"})
public enum DeviceType {

    /**
     * 桌面端
     */
    DESK_TOP("D", "桌面端"),

    /**
     * 移动端
     */
    MOBILE("M", "移动端"),

    /**
     * 平板设备
     */
    TABLET("T", "平板设备"),

    /**
     * 其他设备
     */
    OTHER("O", "其他设备"),

    /**
     * 未登录
     */
    UNKNOWN("U", "未登录");

    /**
     * 类型的数字标识
     */
    public final String identity;

    /**
     * 文字描述
     */
    public final String disc;

    DeviceType(String identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}
