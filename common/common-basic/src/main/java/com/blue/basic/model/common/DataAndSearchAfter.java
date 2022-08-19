package com.blue.basic.model.common;

import java.io.Serializable;
import java.util.List;

import static com.blue.basic.common.base.BlueChecker.isNotNull;
import static java.util.Collections.emptyList;

/**
 * data and search after model
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public final class DataAndSearchAfter<T extends Serializable, A extends Serializable> implements Serializable {

    private static final long serialVersionUID = 5874171661193191540L;

    /**
     * data
     */
    private List<T> data;

    /**
     * search after
     */
    private A searchAfter;

    public DataAndSearchAfter() {
        this.data = emptyList();
        this.searchAfter = null;
    }

    public DataAndSearchAfter(List<T> data, A searchAfter) {
        this.data = isNotNull(data) ? data : emptyList();
        this.searchAfter = searchAfter;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public A getSearchAfter() {
        return searchAfter;
    }

    public void setSearchAfter(A searchAfter) {
        this.searchAfter = searchAfter;
    }

    @Override
    public String toString() {
        return "DataAndSearchAfter{" +
                "data=" + data +
                ", searchAfter=" + searchAfter +
                '}';
    }

}
