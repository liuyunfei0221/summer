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
     * business code
     */
    private Integer code;

    /**
     * response data
     */
    private T data;

    /**
     * response message
     */
    private String message;

    public BlueResponse() {
    }

    public BlueResponse(Integer code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BlueResponse{" +
                "code=" + code +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }

}