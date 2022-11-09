package com.blue.risk.api.model;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.*;
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
    private Long memberId;

    /**
     * target ip
     */
    private String ip;

    /**
     * request method
     */
    private String method;

    /**
     * request uri
     */
    private String uri;

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

    public RiskHit(Long memberId, String ip, String method, String uri, Integer hitType, Long illegalExpiresSecond, Long stamp) {
        if (isInvalidIdentity(memberId) && isBlank(ip))
            throw new BlueException(INVALID_PARAM);

        assertRiskType(hitType, false);

        this.memberId = memberId;
        this.ip = ip;
        this.method = method;
        this.uri = uri;
        this.hitType = hitType;
        this.illegalExpiresSecond = illegalExpiresSecond;
        this.stamp = isNotNull(stamp) ? stamp : TIME_STAMP_GETTER.get();
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(this.memberId) && isBlank(this.ip))
            throw new BlueException(INVALID_PARAM);

        assertRiskType(this.hitType, false);
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
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
                ", method='" + method + '\'' +
                ", uri='" + uri + '\'' +
                ", hitType=" + hitType +
                ", illegalExpiresSecond=" + illegalExpiresSecond +
                ", stamp=" + stamp +
                '}';
    }

}
