package com.blue.base.model.common;

import com.blue.base.inter.Asserter;
import com.blue.base.model.exps.BlueException;

import java.io.Serializable;
import java.util.List;

import static com.blue.base.common.base.BlueChecker.isEmpty;
import static com.blue.base.constant.common.ResponseElement.EMPTY_PARAM;

/**
 * list data param
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class ListParam<T> implements Serializable, Asserter {

    private static final long serialVersionUID = -1390300952756018262L;

    private List<T> data;

    public ListParam() {
    }

    public ListParam(List<T> data) {
        this.data = data;
    }

    @Override
    public void asserts() {
        if (isEmpty(this.data))
            throw new BlueException(EMPTY_PARAM);
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ListParam{" +
                "data=" + data +
                '}';
    }

}
