package com.blue.basic.model.common;

import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.BlueCommonThreshold.MAX_ROWS;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * scroll model request params
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class ScrollModelRequest<T extends Serializable> implements Serializable {

    private static final long serialVersionUID = 8775218133394708404L;

    private static final long MAX_ROWS_PER_REQ = MAX_ROWS.value;

    /**
     * nums per request
     */
    private Long rows;

    /**
     * current identity
     */
    private T identity;

    public ScrollModelRequest() {
    }

    public ScrollModelRequest(Long rows, T identity) {
        if (isNull(rows) || rows < 1L || rows > MAX_ROWS_PER_REQ)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "rows can't be less than 1, max rows per request can't be greater than " + MAX_ROWS.value);

        this.rows = rows;
        this.identity = identity;
    }

    public Long getRows() {
        return rows;
    }

    public void setRows(Long rows) {
        if (isNull(rows) || rows < 1L || rows > MAX_ROWS_PER_REQ)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "rows can't be less than 1, max rows per request can't be greater than " + MAX_ROWS.value);

        this.rows = rows;
    }

    public T getIdentity() {
        return identity;
    }

    public void setIdentity(T identity) {
        this.identity = identity;
    }

    @Override
    public String toString() {
        return "ScrollModelRequest{" +
                "rows=" + rows +
                ", identity=" + identity +
                '}';
    }

}
