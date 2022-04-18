package com.blue.marketing.api.model;

import java.io.Serializable;

import static com.blue.base.common.base.BlueChecker.isNotNull;

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
        this.present = isNotNull(this.reward);
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
                "reward=" + (isNotNull(reward) ? reward.toString() : "null") +
                ", present=" + present +
                '}';
    }
}
