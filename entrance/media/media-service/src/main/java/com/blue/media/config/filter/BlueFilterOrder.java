package com.blue.media.config.filter;

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
    BLUE_REQUEST_ATTR(-1002),

    /**
     * auth
     */
    BLUE_AUTH(-1001),

    /**
     * data report
     */
    BLUE_POST_WITH_DATA_REPORT(-1000);

    public final int order;

    BlueFilterOrder(int order) {
        this.order = order;
    }

}
