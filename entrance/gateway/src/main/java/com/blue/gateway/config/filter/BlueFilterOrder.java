package com.blue.gateway.config.filter;

import static org.springframework.cloud.gateway.filter.WebClientWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER;

/**
 * filter order
 *
 * @author liuyunfei
 */
public enum BlueFilterOrder {

    /**
     * pre with error report
     */
    BLUE_PRE_WITH_ERROR_REPORT(WRITE_RESPONSE_FILTER_ORDER - 7),

    /**
     * risk intercept
     */
    BLUE_ILLEGAL_ASSERT(WRITE_RESPONSE_FILTER_ORDER - 6),

    /**
     * rate limit
     */
    BLUE_RATE_LIMIT(WRITE_RESPONSE_FILTER_ORDER - 5),

    /**
     * request attr
     */
    BLUE_REQUEST_ATTR(WRITE_RESPONSE_FILTER_ORDER - 4),

    /**
     * auth
     */
    BLUE_AUTH(WRITE_RESPONSE_FILTER_ORDER - 3),

    /**
     * turing test
     */
    BLUE_TURING_TEST(WRITE_RESPONSE_FILTER_ORDER - 2),

    /**
     * data report
     */
    BLUE_POST_WITH_DATA_REPORT(WRITE_RESPONSE_FILTER_ORDER - 1),

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
