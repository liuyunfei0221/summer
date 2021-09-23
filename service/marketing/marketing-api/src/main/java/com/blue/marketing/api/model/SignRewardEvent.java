package com.blue.marketing.api.model;

import java.io.Serializable;

/**
 * 签到奖励事件
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class SignRewardEvent implements Serializable {

    private static final long serialVersionUID = 803264513846128171L;

    /**
     * 用户id
     */
    private Long memberId;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 日期
     */
    private Integer day;

    /**
     * 当日奖励
     */
    private DayReward dayReward;

    public SignRewardEvent() {
    }

    public SignRewardEvent(Long memberId, Integer year, Integer month, Integer day, DayReward dayReward) {
        this.memberId = memberId;
        this.year = year;
        this.month = month;
        this.day = day;
        this.dayReward = dayReward;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
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

    public DayReward getDayReward() {
        return dayReward;
    }

    public void setDayReward(DayReward dayReward) {
        this.dayReward = dayReward;
    }

    @Override
    public String toString() {
        return "SignRewardEvent{" +
                "memberId=" + memberId +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", dayReward=" + dayReward +
                '}';
    }

}
