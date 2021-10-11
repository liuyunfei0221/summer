package com.blue.marketing.api.model;

import java.io.Serializable;

/**
 * activity reward event
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class ActivityRewardEvent implements Serializable {

    private static final long serialVersionUID = 7287064070086873373L;

    private Long memberId;

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
