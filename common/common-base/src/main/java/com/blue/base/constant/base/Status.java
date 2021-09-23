package com.blue.base.constant.base;

/**
 * 状态约束
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public enum Status {

    /**
     * 可用
     */
    VALID(1, "可用"),
    /**
     * 禁用
     */
    INVALID(0, "禁用");

    /**
     * 状态标识
     */
    public final int status;

    /**
     * 状态描述
     */
    public final String disc;

    Status(int status, String disc) {
        this.status = status;
        this.disc = disc;
    }

}
