package com.blue.base.model.common;

import java.io.Serializable;

/**
 * String value wrapper
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class StringValueParam implements Serializable {

    private static final long serialVersionUID = -9035987948590829505L;

    private String value;

    public StringValueParam() {
    }

    public StringValueParam(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "StringValueParam{" +
                "value='" + value + '\'' +
                '}';
    }

}
