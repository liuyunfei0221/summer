package com.blue.base.model.exps;

import com.blue.base.constant.common.ResponseElement;

import java.util.Arrays;

import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.constant.common.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.common.SpecialStringElement.EMPTY_DATA;

/**
 * global business exception
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class BlueException extends RuntimeException {

    private static final long serialVersionUID = -5482951655656407756L;

    /**
     * exp for can't process
     */
    private static final ResponseElement ELEMENT = INTERNAL_SERVER_ERROR;

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

    /**
     * replacements
     */
    private String[] replacements;

    public BlueException() {
        this.status = INTERNAL_SERVER_ERROR.status;
        this.code = INTERNAL_SERVER_ERROR.code;
        this.message = INTERNAL_SERVER_ERROR.message;
        this.replacements = null;
    }

    public BlueException(ResponseElement responseElement) {
        this.status = responseElement.status;
        this.code = responseElement.code;
        this.message = responseElement.message;
        this.replacements = null;
    }

    public BlueException(ResponseElement responseElement, String[] replacements) {
        this.status = responseElement.status;
        this.code = responseElement.code;
        this.message = responseElement.message;
        this.replacements = replacements;
    }


    public BlueException(Integer status) {
        this.status = status;
        this.code = status;
        this.message = null;
        this.replacements = null;
    }

    public BlueException(Integer status, Integer code) {
        this.status = status;
        this.code = code;
        this.message = null;
        this.replacements = null;
    }

    public BlueException(Integer status, Integer code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.replacements = null;
    }

    public BlueException(Integer status, Integer code, String message, String[] replacements) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.replacements = replacements;
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

    public String[] getReplacements() {
        return replacements;
    }

    public void setReplacements(String[] replacements) {
        this.replacements = replacements;
    }

    @Override
    public String toString() {
        return "BlueException{" +
                "status=" + status +
                ", code=" + code +
                ", message='" + message + '\'' +
                (isNull(replacements) ? EMPTY_DATA.value : ", replacements=" + Arrays.toString(replacements)) +
                '}';
    }
}
