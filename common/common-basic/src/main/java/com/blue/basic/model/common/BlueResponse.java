package com.blue.basic.model.common;

import java.io.Serializable;

/**
 * response result info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class BlueResponse<T> implements Serializable {

    private static final long serialVersionUID = -5093752294116263164L;

    /**
     * response data
     */
    private T data;

    public BlueResponse() {
    }

    public BlueResponse(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BlueResponse{" +
                ", data=" + data +
                '}';
    }
}
