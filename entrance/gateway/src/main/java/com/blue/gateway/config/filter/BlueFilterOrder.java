package com.blue.gateway.config.filter;

import static org.springframework.cloud.gateway.filter.WebClientWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER;

/**
 * 过滤器优先级
 *
 * @author DarkBlue
 */

public enum BlueFilterOrder {

    /**
     * 异常上报过滤器
     */
    BLUE_ERROR_REPORT(WRITE_RESPONSE_FILTER_ORDER - 6),

    /**
     * 限流过滤器
     */
    BLUE_RATE_LIMIT(WRITE_RESPONSE_FILTER_ORDER - 5),

    /**
     * 非法拦截过滤器
     */
    BLUE_ILLEGAL_INTERCEPT(WRITE_RESPONSE_FILTER_ORDER - 4),

    /**
     * 请求参数过滤器
     */
    BLUE_REQUEST_ATTR(WRITE_RESPONSE_FILTER_ORDER - 3),

    /**
     * 安全过滤器
     */
    BLUE_SECURE(WRITE_RESPONSE_FILTER_ORDER - 2),

    /**
     * 请求/响应体处理及数据上报过滤器
     */
    BLUE_BODY_PROCESS_AND_DATA_REPORT(WRITE_RESPONSE_FILTER_ORDER - 1),

    /**
     * loadbalancer
     */
    BLUE_LOAD_BALANCER_CLIENT(10150),

    /**
     * instance circuit
     */
    BLUE_INSTANCE_CIRCUIT_BREAKER(10151);

    public final int order;

    BlueFilterOrder(int order) {
        this.order = order;
    }

}
