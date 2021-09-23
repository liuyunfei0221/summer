package com.blue.base.model.base;

import java.io.Serializable;

/**
 * 响应封装类
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class BlueResult<T> implements Serializable {

    private static final long serialVersionUID = -5093752294116263164L;

    /**
     * 响应状态码
     */
    private Integer code;

    /**
     * 响应数据体
     */
    private T data;

    /**
     * 响应信息
     */
    private String message;

    public BlueResult() {
    }

    public BlueResult(Integer code, T data, String message) {
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
        return "BlueVO{" +
                "code=" + code +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
