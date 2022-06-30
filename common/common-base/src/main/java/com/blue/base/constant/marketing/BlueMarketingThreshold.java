package com.blue.base.constant.marketing;

/**
 * threshold values or marketing values
 *
 * @author liuyunfei
 */
public enum BlueMarketingThreshold {

    /**
     * maximum expiration time of monthly sign-in information(day)
     */
    MAX_EXPIRE_DAYS_FOR_SIGN(33L);
    
    /**
     * number
     */
    public final long value;

    BlueMarketingThreshold(long value) {
        this.value = value;
    }

}
