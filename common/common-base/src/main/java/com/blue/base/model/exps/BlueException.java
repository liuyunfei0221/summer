package com.blue.base.model.exps;

import com.blue.base.constant.base.ResponseElement;

import java.util.Arrays;

import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;

/**
 * global business exception
 *
 * @author DarkBlue
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
                ", replacements=" + Arrays.toString(replacements) +
                '}';
    }
}
