package com.blue.base.model.base;

import java.io.Serializable;
import java.util.List;

/**
 * page data
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class PageModelResponse<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = -2833276453789009836L;

    /**
     * data element list
     */
    private List<T> data;

    /**
     * total count
     */
    private Long count;

    public PageModelResponse() {
    }

    public PageModelResponse(List<T> data, Long count) {
        this.data = data;
        this.count = count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "PageModelVO{" +
                "data=" + data +
                ", count=" + count +
                '}';
    }

}
