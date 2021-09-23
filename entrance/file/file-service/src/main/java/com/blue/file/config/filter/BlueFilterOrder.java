package com.blue.file.config.filter;

/**
 * 过滤器优先级
 *
 * @author DarkBlue
 */

public enum BlueFilterOrder {

    /**
     * 异常上报过滤器
     */
    BLUE_ERROR_REPORT(-1005),

    /**
     * 限流过滤器
     */
    BLUE_RATE_LIMIT(-1004),

    /**
     * 非法拦截过滤器
     */
    BLUE_ILLEGAL_INTERCEPT(-1003),

    /**
     * 请求参数过滤器
     */
    BLUE_REQUEST_ATTR(-1002),

    /**
     * 安全过滤器
     */
    BLUE_SECURE(-1001),

    /**
     * 请求/响应体处理及数据上报过滤器
     */
    BLUE_BODY_PROCESS_AND_DATA_REPORT(-1000);

    public final int order;

    BlueFilterOrder(int order) {
        this.order = order;
    }

}
