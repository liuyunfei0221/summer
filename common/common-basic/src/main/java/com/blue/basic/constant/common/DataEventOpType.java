package com.blue.basic.constant.common;

import java.io.Serializable;

/**
 * data event operate
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public enum DataEventOpType implements Serializable {

    /**
     * click
     */
    CLICK("click"),

    /**
     * stop over
     */
    STOP_OVER("stopOver");

    /**
     * identity
     */
    public final String identity;

    DataEventOpType(String identity) {
        this.identity = identity;
    }

}
