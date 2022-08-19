package com.blue.marketing.api.model;

import com.blue.basic.serializer.IdentityDeserializer;
import com.blue.basic.serializer.IdentitySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * sign in reward
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class SignRewardEvent implements Serializable {

    private static final long serialVersionUID = 803264513846128171L;

    @JsonSerialize(using = IdentitySerializer.class)
    @JsonDeserialize(using = IdentityDeserializer.class)
    private Long memberId;

    private Integer year;

    private Integer month;

    private Integer day;

    /**
     * reward of current day
     */
    private SignInReward signInReward;

    public SignRewardEvent() {
    }

    public SignRewardEvent(Long memberId, Integer year, Integer month, Integer day, SignInReward signInReward) {
        this.memberId = memberId;
        this.year = year;
        this.month = month;
        this.day = day;
        this.signInReward = signInReward;
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

    public SignInReward getDayReward() {
        return signInReward;
    }

    public void setDayReward(SignInReward signInReward) {
        this.signInReward = signInReward;
    }

    @Override
    public String toString() {
        return "SignRewardEvent{" +
                "memberId=" + memberId +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", dayReward=" + signInReward +
                '}';
    }

}
