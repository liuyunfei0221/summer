package com.blue.base.constant.base;

/**
 * 特殊数值配置
 *
 * @author DarkBlue
 */
public enum ThresholdNumericalValue {

    /**
     * 默认页码
     */
    PAGE(1L, "默认页码"),

    /**
     * 默认每页条数
     */
    ROWS(10L, "默认每页条数"),

    /**
     * 每页最大条数
     */
    MAX_ROWS(100L, "每页最大条数"),

    /**
     * 等待其他线程同步数据时轮询间隔时间
     */
    WAIT_MILLIS_FOR_THREAD_SLEEP(100L, "等待其他线程同步数据时轮询间隔时间"),

    /**
     * redisson同步时其他线程最大等待时间
     */
    MAX_WAIT_MILLIS_FOR_REDISSON_SYNC(1000L, "redisson同步时其他线程最大等待时间"),

    /**
     * 数据库单次查询最大条数
     */
    DB_SELECT(100L, "数据库单次查询最大条数"),

    /**
     * 数据库单次插入最大条数
     */
    DB_INSERT(100L, "数据库单次插入最大条数"),

    /**
     * 成员登录/手机号/邮箱长度下限
     */
    ID_LEN_MIN(8L, "成员登录账号长度下限"),

    /**
     * 成员登录/手机号/邮箱长度上限
     */
    ID_LEN_MAX(64L, "成员登录账号长度上限"),

    /**
     * 手机号长度下限
     */
    PHONE_LEN_MIN(11L, "手机号长度下限"),

    /**
     * 手机号长度上限
     */
    PHONE_LEN_MAX(18L, "手机号长度上限"),

    /**
     * 邮箱长度下限
     */
    EMAIL_LEN_MIN(11L, "邮箱长度下限"),

    /**
     * 邮箱长度上限
     */
    EMAIL_LEN_MAX(64L, "邮箱长度上限"),

    /**
     * 成员登录密码长度下限
     */
    ACS_LEN_MIN(8L, "成员登录密码长度下限"),

    /**
     * 成员登录密码长度上限
     */
    ACS_LEN_MAX(256L, "成员登录密码长度上限"),

    /**
     * 验证码长度上限
     */
    VFC_LEN_MAX(256L, "验证码长度上限"),

    /**
     * 未登录特殊成员id
     */
    NOT_LOGGED_IN_MEMBER_ID(0L, "未登录特殊成员id"),

    /**
     * 未登录特殊角色id
     */
    NOT_LOGGED_IN_ROLE_ID(0L, "未登录特殊角色id"),

    /**
     * 未登录特殊登录时间戳
     */
    NOT_LOGGED_IN_TIME(0L, "未登录特殊登录时间戳"),

    /**
     * 月签到信息最大过期时间/天
     */
    MAX_EXPIRE_DAYS_FOR_SIGN(33L, "月签到信息最大过期时间/天"),

    /**
     * 二维码宽度
     */
    QR_CODE_WIDTH(200L, "二维码宽度"),

    /**
     * 二维码高度
     */
    QR_CODE_HEIGHT(200L, "二维码高度"),

    /**
     * 二维码LOGO宽度
     */
    QR_CODE_LOGO_ROUND_ARCW(20L, "二维码LOGO宽度"),

    /**
     * 二维码LOGO高度
     */
    QR_CODE_LOGO_ROUND_ARCH(20L, "二维码LOGO高度");


    /**
     * 数值
     */
    public final long value;

    /**
     * 描述
     */
    public final String disc;

    ThresholdNumericalValue(long value, String disc) {
        this.value = value;
        this.disc = disc;
    }

}
