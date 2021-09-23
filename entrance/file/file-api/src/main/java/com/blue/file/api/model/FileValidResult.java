package com.blue.file.api.model;

/**
 * 文件校验结果封装
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class FileValidResult {

    /**
     * 是否合法
     */
    private final Boolean valid;

    /**
     * 原文件名
     */
    private final String name;

    /**
     * 信息
     */
    private final String message;

    public FileValidResult(Boolean valid, String name, String message) {
        this.valid = valid;
        this.name = name;
        this.message = message;
    }

    public Boolean getValid() {
        return valid;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
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
