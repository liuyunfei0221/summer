package com.blue.marketing.model;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;
import java.util.Map;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.BlueCommonThreshold.*;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * params for batch insert reward date relation
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
public class RewardDateRelationBatchInsertParam implements Serializable, Asserter {

    private static final long serialVersionUID = 9115541426770608036L;

    private Integer year;

    private Integer month;

    Map<Integer, Long> dayRelations;

    public RewardDateRelationBatchInsertParam() {
    }

    public RewardDateRelationBatchInsertParam(Integer year, Integer month, Map<Integer, Long> dayRelations) {
        this.year = year;
        this.month = month;
        this.dayRelations = dayRelations;
    }

    @Override
    public void asserts() {
        if (isNull(year) || year < ((int) MIN_YEAR.value))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid year");
        if (isNull(month) || month < ((int) MIN_MONTH.value) || month > ((int) MAX_MONTH.value))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid month");
        if (isEmpty(dayRelations) || dayRelations.size() > ((int) MAX_DAY_OF_MONTH.value))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "dayRelations can't be empty or more than 31");
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Map<Integer, Long> getDayRelations() {
        return dayRelations;
    }

    public void setDayRelations(Map<Integer, Long> dayRelations) {
        this.dayRelations = dayRelations;
    }

    @Override
    public String toString() {
        return "RewardDateRelationBatchInsertParam{" +
                "year=" + year +
                ", month=" + month +
                ", dayRelations=" + dayRelations +
                '}';
    }

}
