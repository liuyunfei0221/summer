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
    private List<T> list;

    /**
     * total count
     */
    private Long count;

    public PageModelResponse() {
    }

    public PageModelResponse(List<T> list, Long count) {
        this.list = list;
        this.count = count;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
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
                "list=" + list +
                ", count=" + count +
                '}';
    }

}
