package com.blue.file.api.model;

import java.io.Serializable;

/**
 * 文件上传结果封装
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class FileUploadResult implements Serializable {

    private static final long serialVersionUID = -6671532943914698022L;


    /**
     * 上传后的目标文件名/url
     */
    private final String destination;

    /**
     * 上传前原文件名
     */
    private final String resource;

    /**
     * 上传成功标识
     */
    private final Boolean success;

    /**
     * 处理信息
     */
    private final String message;

    /**
     * 文件大小
     */
    private final Long length;


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

    public String getResource() {
        return resource;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Long getLength() {
        return length;
    }

    @Override
    public String toString() {
        return "FileUploadVO{" +
                "destination='" + destination + '\'' +
                ", resource='" + resource + '\'' +
                ", success=" + success +
                ", message='" + message + '\'' +
                ", length=" + length +
                '}';
    }
}
