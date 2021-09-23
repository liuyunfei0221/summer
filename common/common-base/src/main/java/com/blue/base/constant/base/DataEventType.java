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
     * 统一上报
     */
    UNIFIED("unified", "统一上报"),

    /**
     * 数据埋点
     */
    BURIED_POINT("buriedPoint", "数据埋点");

    /**
     * 标识
     */
    public final String identity;

    /**
     * 描述
     */
    public final String disc;

    DataEventType(String identity, String disc) {
        this.identity = identity;
        this.disc = disc;
    }

}
