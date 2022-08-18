package com.blue.basic.model.common;

import java.io.Serializable;
import java.util.List;

/**
 * scroll data model
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class ScrollModelResponse<T extends Serializable, A extends Serializable> implements Serializable {

    private static final long serialVersionUID = 4330371876098134823L;

    /**
     * data element list
     */
    private List<T> data;

    /**
     * cursor
     */
    private A cursor;

    public ScrollModelResponse() {
    }

    public ScrollModelResponse(List<T> data) {
        this.data = data;
        this.cursor = null;
    }

    public ScrollModelResponse(List<T> data, A cursor) {
        this.data = data;
        this.cursor = cursor;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public A getCursor() {
        return cursor;
    }

    public void setCursor(A cursor) {
        this.cursor = cursor;
    }

    @Override
    public String toString() {
        return "ScrollModelResponse{" +
                "data=" + data +
                ", cursor=" + cursor +
                '}';
    }

}
