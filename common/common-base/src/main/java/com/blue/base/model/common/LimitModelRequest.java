package com.blue.base.model.common;

import com.blue.base.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.constant.base.BlueNumericalValue.*;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
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
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "limit can't be less than 0, max rows per page can't be greater than " + MAX_ROWS.value);
        if (isNull(rows) || rows < 1L || rows > MAX_ROWS_PER_REQ)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "limit can't be less than 0, max rows per page can't be greater than " + MAX_ROWS.value);

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
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "limit can't be less than 0, max rows per page can't be greater than " + MAX_ROWS.value);

        this.limit = limit;
    }

    public void setRows(Long rows) {
        if (isNull(rows) || rows < 1L || rows > MAX_ROWS_PER_REQ)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "limit can't be less than 0, max rows per page can't be greater than " + MAX_ROWS.value);

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
