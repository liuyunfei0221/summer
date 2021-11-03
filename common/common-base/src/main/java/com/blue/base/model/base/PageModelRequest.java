package com.blue.base.model.base;

import java.io.Serializable;

import static com.blue.base.constant.base.BlueNumericalValue.*;
import static com.blue.base.constant.base.CommonException.INVALID_PAGE_PARAM_EXP;
import static java.util.Optional.ofNullable;

/**
 * page model request params
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class PageModelRequest<T> implements Serializable {

    private static final long serialVersionUID = -59225648928098772L;

    private static final long DEFAULT_PAGE = PAGE.value, DEFAULT_ROWS = ROWS.value;
    private static final long MAX_ROWS_PER_REQ = MAX_ROWS.value;

    /**
     * current page
     */
    private Long page;

    /**
     * nums per page
     */
    private Long rows;

    /**
     * differentiated parameter package
     */
    private T param;

    public PageModelRequest() {
    }

    public PageModelRequest(Long page, Long rows, T param) {
        if (page == null || page < 1L)
            throw INVALID_PAGE_PARAM_EXP.exp;
        if (rows == null || rows < 1L || rows > MAX_ROWS_PER_REQ)
            throw INVALID_PAGE_PARAM_EXP.exp;

        this.page = page;
        this.rows = rows;
        this.param = param;
    }

    public Long getPage() {
        return ofNullable(page).orElse(DEFAULT_PAGE);
    }

    public Long getRows() {
        return ofNullable(rows).orElse(DEFAULT_ROWS);
    }

    public Long getLimit() {
        return (getPage() - 1L) * getRows();
    }

    public void setPage(Long page) {
        if (page == null || page < 1L)
            throw INVALID_PAGE_PARAM_EXP.exp;

        this.page = page;
    }

    public void setRows(Long rows) {
        if (rows == null || rows < 1L || rows > MAX_ROWS_PER_REQ)
            throw INVALID_PAGE_PARAM_EXP.exp;

        this.rows = rows;
    }

    public T getParam() {
        return param;
    }

    public void setParam(T param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "PageModelDTO{" +
                "page=" + page +
                ", rows=" + rows +
                ", param=" + param +
                '}';
    }

}
