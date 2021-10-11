package com.blue.marketing.api.model;

import java.io.Serializable;

/**
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class SignInRewardRecord implements Serializable {

    private static final long serialVersionUID = 194111541862815072L;
    /**
     * sign in reward
     */
    private SignInReward signInReward;

    /**
     * already sign in?
     */
    private Boolean signed;

    public SignInRewardRecord(SignInReward signInReward, Boolean signed) {
        this.signInReward = signInReward;
        this.signed = signed;
    }

    public SignInReward getDayReward() {
        return signInReward;
    }

    public void setDayReward(SignInReward signInReward) {
        this.signInReward = signInReward;
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
                "dayReward=" + signInReward +
                ", signed=" + signed +
                '}';
    }
}
