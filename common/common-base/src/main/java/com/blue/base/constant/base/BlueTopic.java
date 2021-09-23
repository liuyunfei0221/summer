package com.blue.base.constant.base;

/**
 * pulsar topic约束
 *
 * @author DarkBlue
 */
public enum BlueTopic {

    /**
     * 非法ip或jwt前置拦截标记
     */
    ILLEGAL_MARK("illegalMark", "前置拦截标记"),

    /**
     * 数据上报通道/前置上报与后置上报通用
     */
    REQUEST_EVENT("requestEvent", "数据上报"),

    /**
     * 认证过期时间刷新
     */
    AUTH_EXPIRE("authExpire", "认证过期时间刷新"),

    /**
     * 本地auth认证过期
     */
    INVALID_LOCAL_AUTH("invalidLocalAuth", "本地auth认证过期"),

    /**
     * 签到数据过期时间刷新
     */
    SIGN_EXPIRE("signExpire", "签到数据过期时间刷新"),

    /**
     * 营销/推广事件
     */
    MARKETING("marketing", "营销/推广事件");


    /**
     * topic名称
     */
    public final String name;

    /**
     * 描述
     */
    public final String disc;

    BlueTopic(String name, String disc) {
        this.name = name;
        this.disc = disc;
    }

}
