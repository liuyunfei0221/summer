package com.blue.base.model.base;

import java.io.Serializable;

/**
 * 加密数据与时间戳的信息封装类
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class DataWrapper implements Serializable {

    private static final long serialVersionUID = 3491515130185040722L;

    /**
     * 数据加密前的json
     */
    private String original;

    /**
     * 时间戳
     */
    private Long timeStamp;

    public DataWrapper() {
    }

    public DataWrapper(String original, Long timeStamp) {
        this.original = original;
        this.timeStamp = timeStamp;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "DataWrapper{" +
                "original='" + original + '\'' +
                ", timeStamp=" + timeStamp +
                '}';
    }

}
