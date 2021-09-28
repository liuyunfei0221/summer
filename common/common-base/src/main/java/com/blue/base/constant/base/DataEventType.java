package com.blue.base.constant.base;

import java.io.Serializable;

/**
 * 数据上报事件类型
 *
 * @author DarkBlue
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
     * 标识
     */
    public final String identity;

    DataEventType(String identity) {
        this.identity = identity;
    }

}
