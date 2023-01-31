package com.blue.basic.model.common;

import java.io.Serializable;

/**
 * Encrypted data and timestamp information
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class DataWrapper implements Serializable {

    private static final long serialVersionUID = 3491515130185040722L;

    /**
     * data json
     */
    private String original;

    /**
     * signature timestamp
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