package com.blue.marketing.api.model;

import com.blue.basic.serializer.IdentityDeserializer;
import com.blue.basic.serializer.IdentitySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * reward date relation info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public class RewardDateRelationInfo implements Serializable {

    private static final long serialVersionUID = 6344022649076039096L;

    @JsonSerialize(using = IdentitySerializer.class)
    @JsonDeserialize(using = IdentityDeserializer.class)
    private Long id;

    private Long rewardId;

    private RewardInfo rewardInfo;

    private Integer year;

    private Integer month;

    private Integer day;

    public RewardDateRelationInfo() {
    }

    public RewardDateRelationInfo(Long id, Long rewardId, RewardInfo rewardInfo, Integer year, Integer month, Integer day) {
        this.id = id;
        this.rewardId = rewardId;
        this.rewardInfo = rewardInfo;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRewardId() {
        return rewardId;
    }

    public void setRewardId(Long rewardId) {
        this.rewardId = rewardId;
    }

    public RewardInfo getRewardInfo() {
        return rewardInfo;
    }

    public void setRewardInfo(RewardInfo rewardInfo) {
        this.rewardInfo = rewardInfo;
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
        return "RewardDateRelationManagerInfo{" +
                "id=" + id +
                ", rewardId=" + rewardId +
                ", rewardInfo=" + rewardInfo +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                '}';
    }

}
