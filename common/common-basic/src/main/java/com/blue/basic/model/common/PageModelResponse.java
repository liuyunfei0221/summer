package com.blue.basic.model.common;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import static com.blue.basic.constant.common.BlueCommonThreshold.MIN_COUNT;
import static java.util.Optional.ofNullable;

/**
 * page data model
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class PageModelResponse<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = -2833276453789009836L;

    private static final long DEFAULT_COUNT = MIN_COUNT.value;

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
        return ofNullable(data).orElseGet(Collections::emptyList);
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Long getCount() {
        return ofNullable(count).orElse(DEFAULT_COUNT);
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
