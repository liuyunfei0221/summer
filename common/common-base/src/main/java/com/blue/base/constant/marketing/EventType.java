package com.blue.base.constant.marketing;

/**
 * 营销事件类型
 *
 * @author DarkBlue
 */
public enum EventType {

    /**
     * 签到奖励
     */
    SIGN_REWARD(1, "签到奖励"),

    /**
     * 活动奖励
     */
    ACTIVITY_REWARD(2, "活动奖励"),

    /**
     * 未知或空或错误类型
     */
    UNKNOWN(-1, "未知或空或错误类型");

    /**
     * 数字标识
     */
    public int identity;

    /**
     * 描述
     */
    public String disc;

    EventType(int identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}
