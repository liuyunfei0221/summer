package com.blue.base.component.exception.handler.model;

import java.util.Arrays;

import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;

/**
 * http status code and result
 *
 * @author DarkBlue
 */
public final class ExceptionInfo {

    private final Integer status;

    private final Integer code;

    private final String[] fillings;

    public ExceptionInfo() {
        this.status = INTERNAL_SERVER_ERROR.code;
        this.code = INTERNAL_SERVER_ERROR.code;
        this.fillings = null;
    }

    public ExceptionInfo(Integer status, Integer code, String[] fillings) {
        this.status = status != null ? status : INTERNAL_SERVER_ERROR.code;
        this.code = code != null ? code : INTERNAL_SERVER_ERROR.code;
        this.fillings = fillings;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getCode() {
        return code;
    }

    public String[] getFillings() {
        return fillings;
    }

    @Override
    public String toString() {
        return "ExceptionHandleInfo{" +
                "status=" + status +
                ", code=" + code +
                ", fillings=" + Arrays.toString(fillings) +
                '}';
    }

}
