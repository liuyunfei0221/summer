package com.blue.basic.constant.event;

import java.io.Serializable;

/**
 * data event attribute key
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public enum DataEventAttrKey implements Serializable {

    /**
     * data event type
     */
    DATA_EVENT_TYPE("dataEventType"),

    /**
     * data event operator type
     */
    DATA_EVENT_OP_TYPE("dataEventOpType");

    /**
     * key
     */
    public final String key;

    DataEventAttrKey(String key) {
        this.key = key;
    }

}
