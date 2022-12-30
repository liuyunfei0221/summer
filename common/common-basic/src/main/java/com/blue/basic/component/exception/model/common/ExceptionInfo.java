package com.blue.basic.component.exception.model.common;

import com.blue.basic.constant.common.ResponseElement;
import com.blue.basic.model.exps.BlueException;

import java.util.Arrays;

import static com.blue.basic.common.base.BlueChecker.isNotNull;
import static com.blue.basic.constant.common.ResponseElement.INTERNAL_SERVER_ERROR;

/**
 * http status code and result
 *
 * @author liuyunfei
 */
public final class ExceptionInfo {

    private final Integer status;

    private final Integer code;

    private final String[] replacements;

    public ExceptionInfo() {
        this.status = INTERNAL_SERVER_ERROR.code;
        this.code = INTERNAL_SERVER_ERROR.code;
        this.replacements = null;
    }

    public ExceptionInfo(BlueException blueException) {
        if (isNotNull(blueException)) {
            this.status = blueException.getStatus();
            this.code = blueException.getCode();
            this.replacements = blueException.getReplacements();
        } else {
            this.status = INTERNAL_SERVER_ERROR.code;
            this.code = INTERNAL_SERVER_ERROR.code;
            this.replacements = null;
        }
    }

    public ExceptionInfo(BlueException blueException, String[] replacements) {
        if (isNotNull(blueException)) {
            this.status = blueException.getStatus();
            this.code = blueException.getCode();
        } else {
            this.status = INTERNAL_SERVER_ERROR.code;
            this.code = INTERNAL_SERVER_ERROR.code;
        }
        this.replacements = replacements;
    }

    public ExceptionInfo(ResponseElement responseElement) {
        if (isNotNull(responseElement)) {
            this.status = responseElement.status;
            this.code = responseElement.code;
        } else {
            this.status = INTERNAL_SERVER_ERROR.code;
            this.code = INTERNAL_SERVER_ERROR.code;
        }

        this.replacements = null;
    }

    public ExceptionInfo(ResponseElement responseElement, String[] replacements) {
        if (isNotNull(responseElement)) {
            this.status = responseElement.status;
            this.code = responseElement.code;
        } else {
            this.status = INTERNAL_SERVER_ERROR.code;
            this.code = INTERNAL_SERVER_ERROR.code;
        }

        this.replacements = replacements;
    }

    public ExceptionInfo(Integer status, Integer code) {
        this.status = isNotNull(status) ? status : INTERNAL_SERVER_ERROR.code;
        this.code = isNotNull(code) ? code : INTERNAL_SERVER_ERROR.code;
        this.replacements = null;
    }

    public ExceptionInfo(Integer status, Integer code, String[] replacements) {
        this.status = isNotNull(status) ? status : INTERNAL_SERVER_ERROR.code;
        this.code = isNotNull(code) ? code : INTERNAL_SERVER_ERROR.code;
        this.replacements = replacements;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getCode() {
        return code;
    }

    public String[] getReplacements() {
        return replacements;
    }

    @Override
    public String toString() {
        return "ExceptionHandleInfo{" +
                "status=" + status +
                ", code=" + code +
                ", replacements=" + Arrays.toString(replacements) +
                '}';
    }

}
