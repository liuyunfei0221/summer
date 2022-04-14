package com.blue.media.api.model;

import java.io.Serializable;

/**
 * media upload result
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class FileUploadResult implements Serializable {

    private static final long serialVersionUID = -6671532943914698022L;

    private String destination;

    private String resource;

    private Boolean success;

    private String message;

    private Long length;

    public FileUploadResult() {
    }

    public FileUploadResult(String destination, String resource, boolean success, String message, Long length) {
        this.destination = destination;
        this.resource = resource;
        this.success = success;
        this.message = message;
        this.length = length;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    @Override
    public String toString() {
        return "FileUploadResult{" +
                "destination='" + destination + '\'' +
                ", resource='" + resource + '\'' +
                ", success=" + success +
                ", message='" + message + '\'' +
                ", length=" + length +
                '}';
    }

}
