package com.blue.marketing.api.model;

import java.io.Serializable;
import java.util.Map;

/**
 * month sign record
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class MonthSignInRewardRecord implements Serializable {

    private static final long serialVersionUID = -5165788568293880953L;

    /**
     * records info of current month
     */
    private final Map<Integer, SignInRewardRecord> record;

    /**
     * sign total
     */
    private final Integer total;

    public MonthSignInRewardRecord(Map<Integer, SignInRewardRecord> record, Integer total) {
        this.record = record;
        this.total = total;
    }

    public Map<Integer, SignInRewardRecord> getRecord() {
        return record;
    }

    public Integer getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "MonthRecord{" +
                "record=" + record +
                ", total=" + total +
                '}';
    }

}
