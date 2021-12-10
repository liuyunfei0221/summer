package com.blue.base.model.base;

import java.io.Serializable;

/**
 * exp response result info
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class ExceptionResponse implements Serializable {

    private static final long serialVersionUID = -1568322065726586876L;

    /**
     * business code
     */
    private Integer code;

    /**
     * response message
     */
    private String message;

    public ExceptionResponse() {
    }

    public ExceptionResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
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
                ", message='" + message + '\'' +
                '}';
    }
}
