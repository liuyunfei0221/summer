package com.blue.basic.model.common;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isNotNull;

/**
 * exp response
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class ExceptionResponse implements Serializable {

    private static final long serialVersionUID = 8910174267505748174L;

    /**
     * business code
     */
    private final Integer code;

    /**
     * response message
     */
    private final String message;

    public ExceptionResponse(Integer code, String message) {
        if (isNotNull(code) && isNotNull(message)) {
            this.code = code;
            this.message = message;
        } else {
            throw new RuntimeException("code or message can't be null");
        }
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
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }

}