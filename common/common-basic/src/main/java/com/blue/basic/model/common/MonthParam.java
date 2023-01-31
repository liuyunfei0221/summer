package com.blue.basic.model.common;

import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.BlueCommonThreshold.MAX_MONTH;
import static com.blue.basic.constant.common.BlueCommonThreshold.MIN_MONTH;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * params for year and month
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class MonthParam extends YearParam implements Serializable {

    private static final long serialVersionUID = 3999342903196580275L;

    private Integer month;

    public MonthParam() {
    }

    public MonthParam(Integer year, Integer month) {
        super(year);
        this.month = month;
    }

    @Override
    public void asserts() {
        if (isNull(month) || month < ((int) MIN_MONTH.value) || month > ((int) MAX_MONTH.value))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid month");

        super.asserts();
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    @Override
    public String toString() {
        return "MonthParam{" +
                "year=" + super.getYear() +
                ", month=" + month +
                '}';
    }

}