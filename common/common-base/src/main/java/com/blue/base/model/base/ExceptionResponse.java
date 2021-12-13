package com.blue.base.model.base;

import java.io.Serializable;

import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;

/**
 * exp response result info
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class ExceptionResponse implements Serializable {

    private static final long serialVersionUID = -1568322065726586876L;

    private final Integer status;

    /**
     * business code
     */
    private final Integer code;

    /**
     * response message
     */
    private final String message;

    public ExceptionResponse() {
        this.status = INTERNAL_SERVER_ERROR.status;
        this.code = INTERNAL_SERVER_ERROR.code;
        this.message = INTERNAL_SERVER_ERROR.message;
    }

    public ExceptionResponse(Integer status, Integer code, String message) {
        if (status != null && code != null && message != null) {
            this.status = status;
            this.code = code;
            this.message = message;
        } else {
            throw new RuntimeException("status or code or message can't be null");
        }
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ExceptionResponse{" +
                "status=" + status +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }

}
