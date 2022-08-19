package com.blue.es.model;

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
public final class EsDataAndSearchAfter<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 973540411020748948L;

    /**
     * data
     */
    private List<T> data;

    /**
     * search after
     */
    private List<String> searchAfters;

    public EsDataAndSearchAfter() {
        this.data = emptyList();
        this.searchAfters = emptyList();
    }

    public EsDataAndSearchAfter(List<T> data, List<String> searchAfters) {
        this.data = isNotNull(data) ? data : emptyList();
        this.searchAfters = isNotNull(searchAfters) ? searchAfters : emptyList();
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public List<String> getSearchAfters() {
        return searchAfters;
    }

    public void setSearchAfters(List<String> searchAfters) {
        this.searchAfters = searchAfters;
    }

    @Override
    public String toString() {
        return "EsDataAndSearchAfter{" +
                "data=" + data +
                ", searchAfters=" + searchAfters +
                '}';
    }

}
