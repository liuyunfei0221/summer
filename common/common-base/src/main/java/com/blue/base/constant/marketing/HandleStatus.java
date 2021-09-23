package com.blue.base.constant.marketing;

/**
 * 状态约束
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public enum HandleStatus {

    /**
     * 已处理
     */
    HANDLED(1, "已处理"),
    /**
     * 未处理
     */
    BROKEN(0, "未处理");

    /**
     * 状态标识
     */
    public final int status;

    /**
     * 状态描述
     */
    public final String disc;

    HandleStatus(int status, String disc) {
        this.status = status;
        this.disc = disc;
    }

}
