package com.blue.base.model.common;

import java.io.Serializable;

/**
 * String data wrapper
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class StringDataParam implements Serializable {

    private static final long serialVersionUID = -9035987948590829505L;

    private String data;

    public StringDataParam() {
    }

    public StringDataParam(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "StringDataParam{" +
                "data='" + data + '\'' +
                '}';
    }

}
