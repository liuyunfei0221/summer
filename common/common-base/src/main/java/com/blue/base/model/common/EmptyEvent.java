package com.blue.base.model.common;

import java.io.Serializable;

import static com.blue.base.constant.common.SpecialStringElement.EMPTY_DATA;

/**
 * non value wrapper
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public class EmptyEvent implements Serializable {
    private static final long serialVersionUID = -3647476377407752861L;

    private String value = EMPTY_DATA.value;

    public EmptyEvent() {
    }

    public EmptyEvent(String value) {
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
