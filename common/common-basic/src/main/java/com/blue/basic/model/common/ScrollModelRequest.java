package com.blue.basic.model.common;

import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.BlueCommonThreshold.MAX_ROWS;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * scroll request model
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
     * cursor
     */
    private T cursor;

    public ScrollModelRequest() {
    }

    public ScrollModelRequest(Long rows, T cursor) {
        if (isNull(rows) || rows < 1L || rows > MAX_ROWS_PER_REQ)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "rows can't be less than 1, max rows per request can't be greater than " + MAX_ROWS.value);

        this.rows = rows;
        this.cursor = cursor;
    }

    public Long getRows() {
        return rows;
    }

    public void setRows(Long rows) {
        if (isNull(rows) || rows < 1L || rows > MAX_ROWS_PER_REQ)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "rows can't be less than 1, max rows per request can't be greater than " + MAX_ROWS.value);

        this.rows = rows;
    }

    public T getCursor() {
        return cursor;
    }

    public void setCursor(T cursor) {
        this.cursor = cursor;
    }

    @Override
    public String toString() {
        return "ScrollModelRequest{" +
                "rows=" + rows +
                ", cursor=" + cursor +
                '}';
    }

}
