package com.blue.verify.config.filter;

/**
 * filter order
 *
 * @author DarkBlue
 */

public enum BlueFilterOrder {

    /**
     * error report
     */
    BLUE_ERROR_REPORT(-1004),

    /**
     * rate limit
     */
    BLUE_RATE_LIMIT(-1003),

    /**
     * illegal intercept
     */
    BLUE_ILLEGAL_INTERCEPT(-1002),

    /**
     * request attr
     */
    BLUE_REQUEST_ATTR(-1001),

    /**
     * data report
     */
    BLUE_BODY_PROCESS_AND_DATA_REPORT(-1000);

    public final int order;

    BlueFilterOrder(int order) {
        this.order = order;
    }

}
