package com.blue.base.component.exception.handler.model;

import com.blue.base.constant.base.ResponseElement;
import com.blue.base.model.exps.BlueException;

import java.util.Arrays;

import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;

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
        if (blueException != null) {
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
        if (blueException != null) {
            this.status = blueException.getStatus();
            this.code = blueException.getCode();
        } else {
            this.status = INTERNAL_SERVER_ERROR.code;
            this.code = INTERNAL_SERVER_ERROR.code;
        }
        this.replacements = replacements;
    }

    public ExceptionInfo(ResponseElement responseElement) {
        if (responseElement != null) {
            this.status = responseElement.status;
            this.code = responseElement.code;
        } else {
            this.status = INTERNAL_SERVER_ERROR.code;
            this.code = INTERNAL_SERVER_ERROR.code;
        }

        this.replacements = null;
    }

    public ExceptionInfo(ResponseElement responseElement, String[] replacements) {
        if (responseElement != null) {
            this.status = responseElement.status;
            this.code = responseElement.code;
        } else {
            this.status = INTERNAL_SERVER_ERROR.code;
            this.code = INTERNAL_SERVER_ERROR.code;
        }

        this.replacements = replacements;
    }

    public ExceptionInfo(Integer status, Integer code) {
        this.status = status != null ? status : INTERNAL_SERVER_ERROR.code;
        this.code = code != null ? code : INTERNAL_SERVER_ERROR.code;
        this.replacements = null;
    }

    public ExceptionInfo(Integer status, Integer code, String[] replacements) {
        this.status = status != null ? status : INTERNAL_SERVER_ERROR.code;
        this.code = code != null ? code : INTERNAL_SERVER_ERROR.code;
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
