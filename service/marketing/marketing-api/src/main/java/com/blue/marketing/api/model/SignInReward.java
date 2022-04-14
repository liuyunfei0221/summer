package com.blue.marketing.api.model;

import java.io.Serializable;

/**
 * sign in reward info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class SignInReward implements Serializable {

    private static final long serialVersionUID = 5141433813644232691L;

    /**
     * reward info
     */
    private final RewardInfo reward;

    /**
     * is present reward?
     */
    private final Boolean present;

    public SignInReward(RewardInfo reward) {
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
