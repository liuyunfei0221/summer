package com.blue.basic.model.common;

import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.BlueCommonThreshold.*;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static java.util.Optional.ofNullable;

/**
 * limit model request params
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class LimitModelRequest<T> implements Serializable {

    private static final long serialVersionUID = 1173597038902781191L;

    private static final long DEFAULT_LIMIT = LIMIT.value, DEFAULT_ROWS = ROWS.value;
    private static final long MAX_ROWS_PER_REQ = MAX_ROWS.value;

    /**
     * current limit
     */
    private Long limit;

    /**
     * rows
     */
    private Long rows;

    /**
     * differentiated parameter package
     */
    private T param;

    public LimitModelRequest() {
    }

    public LimitModelRequest(Long limit, Long rows, T param) {
        if (isNull(limit) || limit < 0L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "limit can't be less than 0, max rows per page can't be greater than " + MAX_ROWS_PER_REQ);
        if (isNull(rows) || rows < 1L || rows > MAX_ROWS_PER_REQ)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "limit can't be less than 0, max rows per page can't be greater than " + MAX_ROWS_PER_REQ);

        this.limit = limit;
        this.rows = rows;
        this.param = param;
    }

    public Long getLimit() {
        return ofNullable(limit).orElse(DEFAULT_LIMIT);
    }

    public Long getRows() {
        return ofNullable(rows).orElse(DEFAULT_ROWS);
    }

    public void setLimit(Long limit) {
        if (isNull(limit) || limit < 0L)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "limit can't be less than 0, max rows per page can't be greater than " + MAX_ROWS_PER_REQ);

        this.limit = limit;
    }

    public void setRows(Long rows) {
        if (isNull(rows) || rows < 1L || rows > MAX_ROWS_PER_REQ)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "limit can't be less than 0, max rows per page can't be greater than " + MAX_ROWS_PER_REQ);

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
                "limit=" + limit +
                ", rows=" + rows +
                ", param=" + param +
                '}';
    }

}
