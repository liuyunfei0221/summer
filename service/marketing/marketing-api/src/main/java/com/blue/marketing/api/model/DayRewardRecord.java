package com.blue.marketing.api.model;

import java.io.Serializable;

/**
 * 历史日签到信息封装
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class DayRewardRecord implements Serializable {

    private static final long serialVersionUID = 194111541862815072L;
    /**
     * 日签到奖励
     */
    private DayReward dayReward;

    /**
     * 是否签到
     */
    private Boolean signed;

    public DayRewardRecord(DayReward dayReward, Boolean signed) {
        this.dayReward = dayReward;
        this.signed = signed;
    }

    public DayReward getDayReward() {
        return dayReward;
    }

    public void setDayReward(DayReward dayReward) {
        this.dayReward = dayReward;
    }

    public Boolean getSigned() {
        return signed;
    }

    public void setSigned(Boolean signed) {
        this.signed = signed;
    }

    @Override
    public String toString() {
        return "DayRewardRecordDTO{" +
                "dayReward=" + dayReward +
                ", signed=" + signed +
                '}';
    }
}
