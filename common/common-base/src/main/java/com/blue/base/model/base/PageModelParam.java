package com.blue.base.model.base;

import com.blue.base.constant.base.ResponseElement;
import com.blue.base.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ThresholdNumericalValue.*;
import static java.util.Optional.ofNullable;

/**
 * 分页参数封装
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class PageModelParam<T> implements Serializable {

    private static final long serialVersionUID = -59225648928098772L;

    private static final ResponseElement RESPONSE_ELEMENT = BAD_REQUEST;

    private static final long DEFAULT_PAGE = PAGE.value, DEFAULT_ROWS = ROWS.value;
    private static final long MAX_ROWS_PER_REQ = MAX_ROWS.value;

    /**
     * 当前页
     */
    private Long page;

    /**
     * 每页条数
     */
    private Long rows;

    /**
     * 差异化参数封装
     */
    private T param;

    public PageModelParam() {
    }

    public PageModelParam(Long page, Long rows, T param) {
        if (page == null || page < 1L)
            throw new BlueException(RESPONSE_ELEMENT.status, RESPONSE_ELEMENT.code, "当前页不能小于1");
        if (rows == null || rows < 1L || rows > MAX_ROWS_PER_REQ)
            throw new BlueException(RESPONSE_ELEMENT.status, RESPONSE_ELEMENT.code, "每页条数不能超过" + MAX_ROWS_PER_REQ);

        this.page = page;
        this.rows = rows;
        this.param = param;
    }

    public Long getPage() {
        return ofNullable(page).orElse(DEFAULT_PAGE);
    }

    public void setPage(Long page) {
        if (page == null || page < 1L)
            throw new BlueException(RESPONSE_ELEMENT.status, RESPONSE_ELEMENT.code, "当前页不能小于1");

        this.page = page;
    }

    public Long getRows() {
        return ofNullable(rows).orElse(DEFAULT_ROWS);
    }

    public void setRows(Long rows) {
        if (rows == null || rows < 1L || rows > MAX_ROWS_PER_REQ)
            throw new BlueException(RESPONSE_ELEMENT.status, RESPONSE_ELEMENT.code, "每页条数不能超过" + MAX_ROWS_PER_REQ);

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
