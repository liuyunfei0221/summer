package com.blue.marketing.model;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.BlueCommonThreshold.*;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * params for insert a new reward date relation
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
public class RewardDateRelationInsertParam implements Serializable, Asserter {

    private static final long serialVersionUID = -4099297009366670202L;

    private Long rewardId;

    private Integer year;

    private Integer month;

    private Integer day;

    public RewardDateRelationInsertParam() {
    }

    public RewardDateRelationInsertParam(Long rewardId, Integer year, Integer month, Integer day) {
        this.rewardId = rewardId;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(this.rewardId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid rewardId");
        if (isNull(year) || year < ((int) MIN_YEAR.value))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid year");
        if (isNull(month) || month < ((int) MIN_MONTH.value) || month > ((int) MAX_MONTH.value))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid month");
        if (isNull(day) || day < ((int) MIN_DAY_OF_MONTH.value) || day > ((int) MAX_DAY_OF_MONTH.value))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid day");
    }

    public Long getRewardId() {
        return rewardId;
    }

    public void setRewardId(Long rewardId) {
        this.rewardId = rewardId;
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

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "RewardDateRelationInsertParam{" +
                "rewardId=" + rewardId +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                '}';
    }

}
