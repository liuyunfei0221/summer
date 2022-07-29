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

    private static final long serialVersionUID = 4100451335116609251L;

    /**
     * data element list
     */
    private List<T> data;

    /**
     * current identity
     */
    private A identity;

    public ScrollModelResponse() {
    }

    public ScrollModelResponse(List<T> data, A identity) {
        this.data = data;
        this.identity = identity;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public A getIdentity() {
        return identity;
    }

    public void setIdentity(A identity) {
        this.identity = identity;
    }

    @Override
    public String toString() {
        return "ScrollModelResponse{" +
                "data=" + data +
                ", identity=" + identity +
                '}';
    }

}
