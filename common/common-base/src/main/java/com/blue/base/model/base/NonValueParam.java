package com.blue.base.model.base;

import java.io.Serializable;

/**
 * non value wrapper
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public class NonValueParam implements Serializable {
    private static final long serialVersionUID = -3647476377407752861L;

    private String value = "";

    public NonValueParam() {
    }

    public NonValueParam(String value) {
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
        return "NonValueParam{" +
                "value='" + value + '\'' +
                '}';
    }
}
