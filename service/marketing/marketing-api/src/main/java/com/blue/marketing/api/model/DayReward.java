package com.blue.marketing.api.model;

import java.io.Serializable;

/**
 * 当日签到奖励信息封装
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class DayReward implements Serializable {

    private static final long serialVersionUID = 5141433813644232691L;

    /**
     * 当日签到奖励
     */
    private final RewardInfo reward;

    /**
     * 是否存在奖励
     */
    private final Boolean present;

    public DayReward(RewardInfo reward) {
        this.reward = reward;
        this.present = this.reward != null;
    }

    public RewardInfo getReward() {
        return reward;
    }

    public Boolean getPresent() {
        return present;
    }

    @Override
    public String toString() {
        return "DayRewardVO{" +
                "reward=" + (reward != null ? reward.toString() : "null") +
                ", present=" + present +
                '}';
    }
}
