package com.blue.media.api.model;

import com.blue.basic.serializer.Long2StringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * media upload result
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class FileUploadResult implements Serializable {

    private static final long serialVersionUID = -6671532943914698022L;

    /**
     * attachment type
     *
     * @see com.blue.basic.constant.media.AttachmentType
     */
    private Integer type;

    private String destination;

    private String resource;

    private Boolean success;

    private String message;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long length;

    public FileUploadResult() {
    }

    public FileUploadResult(Integer type, String destination, String resource, boolean success, String message, Long length) {
        this.type = type;
        this.destination = destination;
        this.resource = resource;
        this.success = success;
        this.message = message;
        this.length = length;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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
                "type=" + type +
                ", destination='" + destination + '\'' +
                ", resource='" + resource + '\'' +
                ", success=" + success +
                ", message='" + message + '\'' +
                ", length=" + length +
                '}';
    }

}
