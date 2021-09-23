package com.blue.base.constant.base;


/**
 * 非法标记原因
 *
 * @author DarkBlue
 */
public enum IllegalReason {

    /**
     * 请求过多
     */
    TOO_MANY_REQUEST("tooManyRequest", "请求过多"),

    /**
     * 非法参数过多
     */
    TOO_MANY_ILL_REQUEST("tooManyIllRequest", "非法请求过多"),

    /**
     * 未知原因
     */
    UNKNOWN("unknown", "未知原因");


    /**
     * 原因
     */
    public final String reason;

    /**
     * 描述
     */
    public final String disc;

    IllegalReason(String reason, String disc) {
        this.reason = reason;
        this.disc = disc;
    }

}
