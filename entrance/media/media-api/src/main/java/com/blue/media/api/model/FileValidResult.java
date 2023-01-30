package com.blue.media.api.model;

import java.io.Serializable;

/**
 * media valid result
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class FileValidResult implements Serializable {

    private static final long serialVersionUID = -189012325018732536L;

    private Boolean valid;

    private String name;

    private String message;

    public FileValidResult() {
    }

    public FileValidResult(Boolean valid, String name, String message) {
        this.valid = valid;
        this.name = name;
        this.message = message;
    }

    public Boolean getValid() {
        return valid;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "FileValidResultDTO{" +
                "valid=" + valid +
                ", name='" + name + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

}