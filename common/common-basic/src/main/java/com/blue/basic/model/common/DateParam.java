package com.blue.basic.model.common;

import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.BlueCommonThreshold.MAX_DAY_OF_MONTH;
import static com.blue.basic.constant.common.BlueCommonThreshold.MIN_DAY_OF_MONTH;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * params of date
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public class DateParam extends YearAndMonthParam implements Serializable {

    private static final long serialVersionUID = 770233259803754878L;

    private Integer day;

    public DateParam() {
    }

    public DateParam(Integer year, Integer month, Integer day) {
        super(year, month);
        this.day = day;
    }

    @Override
    public void asserts() {
        if (isNull(day) || day < ((int) MIN_DAY_OF_MONTH.value) || day > ((int) MAX_DAY_OF_MONTH.value))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid day");
        super.asserts();
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "DateParam{" +
                "year=" + super.getYear() +
                ", month=" + super.getMonth() +
                ", day=" + day +
                '}';
    }
}
