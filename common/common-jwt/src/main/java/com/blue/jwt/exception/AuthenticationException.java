package com.blue.jwt.exception;

/**
 * jwt业务异常
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class AuthenticationException extends RuntimeException {

    private static final long serialVersionUID = 1998280956652095252L;

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
