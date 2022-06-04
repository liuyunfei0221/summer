package com.blue.event.config.filter;

/**
 * filter order
 *
 * @author liuyunfei
 */
public enum BlueFilterOrder {

    /**
     * pre with error report
     */
    BLUE_PRE_WITH_ERROR_REPORT(-1005),

    /**
     * risk intercept
     */
    BLUE_ILLEGAL_ASSERT(-1004),

    /**
     * rate limit
     */
    BLUE_RATE_LIMIT(-1003),

    /**
     * request attr
     */
    BLUE_REQUEST_ATTR(-1002);

    public final int order;

    BlueFilterOrder(int order) {
        this.order = order;
    }

}
