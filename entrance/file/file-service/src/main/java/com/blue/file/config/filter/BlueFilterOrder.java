package com.blue.file.config.filter;

/**
 * filter order
 *
 * @author DarkBlue
 */

public enum BlueFilterOrder {

    /**
     * error report
     */
    BLUE_ERROR_REPORT(-1005),

    /**
     * rate limit
     */
    BLUE_RATE_LIMIT(-1004),

    /**
     * illegal intercept
     */
    BLUE_ILLEGAL_INTERCEPT(-1003),

    /**
     * request attr
     */
    BLUE_REQUEST_ATTR(-1002),

    /**
     * secure
     */
    BLUE_SECURE(-1001),

    /**
     * data report
     */
    BLUE_BODY_PROCESS_AND_DATA_REPORT(-1000);

    public final int order;

    BlueFilterOrder(int order) {
        this.order = order;
    }

}
