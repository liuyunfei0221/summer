package com.blue.basic.model.common;

import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.BlueCommonThreshold.*;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static java.util.Optional.ofNullable;

/**
 * page request model
 *
 * @author liuyunfei
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
     * differentiated condition
     */
    private T condition;

    public PageModelRequest() {
    }

    public PageModelRequest(Long page, Long rows, T condition) {
        if (isNull(page) || page < 1L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "page can't be less than 1, max rows per page can't be greater than " + MAX_ROWS_PER_REQ);
        if (isNull(rows) || rows < 1L || rows > MAX_ROWS_PER_REQ)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "page can't be less than 1, max rows per page can't be greater than " + MAX_ROWS_PER_REQ);

        this.page = page;
        this.rows = rows;
        this.condition = condition;
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
        if (isNull(page) || page < 1L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "page can't be less than 1, max rows per page can't be greater than " + MAX_ROWS_PER_REQ);

        this.page = page;
    }

    public void setRows(Long rows) {
        if (isNull(rows) || rows < 1L || rows > MAX_ROWS_PER_REQ)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "page can't be less than 1, max rows per page can't be greater than " + MAX_ROWS_PER_REQ);

        this.rows = rows;
    }

    public T getCondition() {
        return condition;
    }

    public void setCondition(T condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "PageModelRequest{" +
                "page=" + page +
                ", rows=" + rows +
                ", condition=" + condition +
                '}';
    }

}