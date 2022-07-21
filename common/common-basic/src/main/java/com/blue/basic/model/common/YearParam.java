package com.blue.basic.model.common;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.BlueCommonThreshold.MIN_YEAR;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * params year
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class YearParam implements Serializable, Asserter {

    private static final long serialVersionUID = 7511515020441432590L;

    private Integer year;

    public YearParam() {
    }

    public YearParam(Integer year) {
        this.year = year;
    }

    @Override
    public void asserts() {
        if (isNull(year) || year < ((int) MIN_YEAR.value))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid year");
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "YearParam{" +
                "year=" + year +
                '}';
    }

}
