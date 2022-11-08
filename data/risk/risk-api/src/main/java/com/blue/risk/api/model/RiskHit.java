package com.blue.risk.api.model;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static com.blue.basic.common.base.BlueChecker.isNotNull;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.common.base.ConstantProcessor.assertRiskType;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;

/**
 * risk hit
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class RiskHit implements Serializable, Asserter {

    private static final long serialVersionUID = 7500950585002560979L;

    /**
     * member id str
     */
    private String memberId;

    /**
     * target ip
     */
    private String ip;

    /**
     * resource for intercept
     */
    private String resourceKey;

    /**
     * hit type
     *
     * @see com.blue.basic.constant.risk.RiskType
     */
    private Integer hitType;

    /**
     * expire seconds
     */
    private Long illegalExpiresSecond;

    /**
     * stamp(second)
     */
    private Long stamp;

    public RiskHit(String memberId, String ip, String resourceKey, Integer hitType, Long illegalExpiresSecond, Long stamp) {
        if (isBlank(memberId) && isBlank(ip))
            throw new BlueException(INVALID_PARAM);

        assertRiskType(hitType, false);

        this.memberId = memberId;
        this.ip = ip;
        this.resourceKey = resourceKey;
        this.hitType = hitType;
        this.illegalExpiresSecond = illegalExpiresSecond;
        this.stamp = isNotNull(stamp) ? stamp : TIME_STAMP_GETTER.get();
    }

    @Override
    public void asserts() {
        if (isBlank(this.memberId) || isBlank(this.ip))
            throw new BlueException(INVALID_PARAM);

        assertRiskType(this.hitType, false);
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getResourceKey() {
        return resourceKey;
    }

    public void setResourceKey(String resourceKey) {
        this.resourceKey = resourceKey;
    }

    public Integer getHitType() {
        return hitType;
    }

    public void setHitType(Integer hitType) {
        this.hitType = hitType;
    }

    public Long getIllegalExpiresSecond() {
        return illegalExpiresSecond;
    }

    public void setIllegalExpiresSecond(Long illegalExpiresSecond) {
        this.illegalExpiresSecond = illegalExpiresSecond;
    }

    public Long getStamp() {
        return stamp;
    }

    public void setStamp(Long stamp) {
        this.stamp = stamp;
    }

    @Override
    public String toString() {
        return "RiskHit{" +
                "memberId='" + memberId + '\'' +
                ", ip='" + ip + '\'' +
                ", resourceKey='" + resourceKey + '\'' +
                ", hitType=" + hitType +
                ", illegalExpiresSecond=" + illegalExpiresSecond +
                ", stamp=" + stamp +
                '}';
    }

}
