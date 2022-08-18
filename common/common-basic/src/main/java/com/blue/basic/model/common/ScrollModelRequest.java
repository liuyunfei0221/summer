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
public final class ScrollModelRequest<T extends Serializable, A extends Serializable> implements Serializable {

    private static final long serialVersionUID = 8775218133394708404L;

    private static final long MAX_ROWS_PER_REQ = MAX_ROWS.value;

    private static final Long FROM = 0L;

    /**
     * nums per request
     */
    private Long rows;

    /**
     * differentiated condition
     */
    private T condition;

    /**
     * search after cursor
     */
    private A cursor;

    public ScrollModelRequest() {
    }

    public ScrollModelRequest(Long rows, T condition, A cursor) {
        if (isNull(rows) || rows < 1L || rows > MAX_ROWS_PER_REQ)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "rows can't be less than 1, max rows per request can't be greater than " + MAX_ROWS.value);

        this.rows = rows;
        this.condition = condition;
        this.cursor = cursor;
    }

    public Long getFrom() {
        return FROM;
    }

    public Long getRows() {
        return rows;
    }

    public void setRows(Long rows) {
        if (isNull(rows) || rows < 1L || rows > MAX_ROWS_PER_REQ)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "rows can't be less than 1, max rows per request can't be greater than " + MAX_ROWS.value);

        this.rows = rows;
    }

    public T getCondition() {
        return condition;
    }

    public void setCondition(T condition) {
        this.condition = condition;
    }

    public A getCursor() {
        return cursor;
    }

    public void setCursor(A cursor) {
        this.cursor = cursor;
    }

    @Override
    public String toString() {
        return "ScrollModelRequest{" +
                "rows=" + rows +
                ", condition=" + condition +
                ", cursor=" + cursor +
                '}';
    }

}
