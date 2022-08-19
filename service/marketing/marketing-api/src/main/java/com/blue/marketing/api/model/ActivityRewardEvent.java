package com.blue.marketing.api.model;

import com.blue.basic.serializer.IdentityDeserializer;
import com.blue.basic.serializer.IdentitySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * activity reward event
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class ActivityRewardEvent implements Serializable {

    private static final long serialVersionUID = 7287064070086873373L;

    @JsonSerialize(using = IdentitySerializer.class)
    @JsonDeserialize(using = IdentityDeserializer.class)
    private Long memberId;

    @JsonSerialize(using = IdentitySerializer.class)
    @JsonDeserialize(using = IdentityDeserializer.class)
    private Long activityId;

    private RewardInfo rewardInfo;

    public ActivityRewardEvent() {
    }

    public ActivityRewardEvent(Long memberId, Long activityId, RewardInfo rewardInfo) {
        this.memberId = memberId;
        this.activityId = activityId;
        this.rewardInfo = rewardInfo;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getActivityId() {
        return activityId;
    }

    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }

    public RewardInfo getRewardInfo() {
        return rewardInfo;
    }

    public void setRewardInfo(RewardInfo rewardInfo) {
        this.rewardInfo = rewardInfo;
    }

    @Override
    public String toString() {
        return "ActivityRewardEvent{" +
                "memberId=" + memberId +
                ", activityId=" + activityId +
                ", rewardInfo=" + rewardInfo +
                '}';
    }

}
