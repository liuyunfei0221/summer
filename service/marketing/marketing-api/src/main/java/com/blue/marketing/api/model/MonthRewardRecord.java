package com.blue.marketing.api.model;

import java.io.Serializable;
import java.util.Map;

/**
 * 月签到信息记录汇总
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class MonthRewardRecord implements Serializable {

    private static final long serialVersionUID = -5165788568293880953L;

    /**
     * 月签到记录
     */
    private final Map<Integer, DayRewardRecord> record;

    /**
     * 当月总签到次数
     */
    private final Integer total;

    public MonthRewardRecord(Map<Integer, DayRewardRecord> record, Integer total) {
        this.record = record;
        this.total = total;
    }

    public Map<Integer, DayRewardRecord> getRecord() {
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
