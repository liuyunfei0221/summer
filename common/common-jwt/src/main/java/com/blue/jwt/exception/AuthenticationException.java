package com.blue.jwt.exception;

import com.blue.jwt.constant.JwtResponseElement;

/**
 * jwt exception
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class AuthenticationException extends RuntimeException {

    private static final long serialVersionUID = 1998280956652095252L;

    /**
     * http status
     */
    private Integer status;

    /**
     * business code
     */
    private Integer code;

    /**
     * message
     */
    private String message;

    public AuthenticationException(JwtResponseElement jwtResponseElement) {
        this.status = jwtResponseElement.status;
        this.code = jwtResponseElement.code;
        this.message = jwtResponseElement.message;
    }

    public AuthenticationException(Integer status, Integer code, String message) {
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
        return "AuthenticationException{" +
                "status=" + status +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }

}
