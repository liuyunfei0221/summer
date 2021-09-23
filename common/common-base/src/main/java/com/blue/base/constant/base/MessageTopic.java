package com.blue.base.constant.base;

/**
 * topic名称信息
 *
 * @author DarkBlue
 */
public enum MessageTopic {

    /**
     * 前置拦截
     */
    BEFORE_INTERCEPT("beforeIntercept", "beforeInterceptGroup"),

    /**
     * 后置拦截
     */
    AFTER_INTERCEPT("afterIntercept", "afterInterceptGroup"),

    /**
     * 认证过期刷新
     */
    AUTH_EXPIRE("authExpire", "authExpireGroup"),

    /**
     * 签到过期刷新
     */
    SIGN_EXPIRE("signExpire", "signExpireGroup"),

    /**
     * 营销事件
     */
    MARKETING_EVENT("marketing", "marketingGroup");

    /**
     * topic名称
     */
    public final String topic;

    /**
     * 消费者组前缀
     */
    public final String group;

    MessageTopic(String topic, String group) {
        this.topic = topic;
        this.group = group;
    }

}
