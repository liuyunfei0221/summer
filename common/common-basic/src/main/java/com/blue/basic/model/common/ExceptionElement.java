package com.blue.basic.model.common;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isNotNull;
import static com.blue.basic.constant.common.ResponseElement.INTERNAL_SERVER_ERROR;

/**
 * exp response element
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class ExceptionElement implements Serializable {

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

    public ExceptionElement() {
        this.status = INTERNAL_SERVER_ERROR.status;
        this.code = INTERNAL_SERVER_ERROR.code;
        this.message = INTERNAL_SERVER_ERROR.message;
    }

    public ExceptionElement(Integer status, Integer code, String message) {
        if (isNotNull(status) && isNotNull(code) && isNotNull(message)) {
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
        return "ExceptionElement{" +
                "status=" + status +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }

}
