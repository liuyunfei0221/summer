package com.blue.base.model.exps;

import com.blue.base.constant.base.ResponseElement;

import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;

/**
 * 业务异常
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class BlueException extends RuntimeException {

    private static final long serialVersionUID = -5482951655656407756L;

    /**
     * 不能处理时的异常
     */
    private static final ResponseElement ELEMENT = INTERNAL_SERVER_ERROR;

    /**
     * 响应状态码
     */
    private Integer status;

    /**
     * 响应业务码
     */
    private Integer code;

    /**
     * 异常信息
     */
    private String message;

    public BlueException() {
        this.status = INTERNAL_SERVER_ERROR.status;
        this.code = INTERNAL_SERVER_ERROR.code;
        this.message = INTERNAL_SERVER_ERROR.message;
    }

    public BlueException(Integer status, Integer code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "BlueException{" +
                "status=" + status +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }

}
