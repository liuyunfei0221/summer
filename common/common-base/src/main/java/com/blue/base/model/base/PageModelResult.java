package com.blue.base.model.base;

import java.io.Serializable;
import java.util.List;

/**
 * 响应分页数据封装
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class PageModelResult<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = -2833276453789009836L;

    /**
     * 数据列表
     */
    private List<T> list;

    /**
     * 总条数
     */
    private Long count;

    public PageModelResult() {
    }

    public PageModelResult(List<T> list, Long count) {
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
