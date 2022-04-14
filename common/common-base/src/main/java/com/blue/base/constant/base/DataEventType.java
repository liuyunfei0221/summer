package com.blue.base.constant.base;

import java.io.Serializable;

/**
 * data event
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public enum DataEventType implements Serializable {

    /**
     * unified
     */
    UNIFIED("unified"),

    /**
     * buriedPoint
     */
    BURIED_POINT("buriedPoint");

    /**
     * identity
     */
    public final String identity;

    DataEventType(String identity) {
        this.identity = identity;
    }

}
