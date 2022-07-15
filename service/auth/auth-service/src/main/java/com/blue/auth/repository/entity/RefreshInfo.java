package com.blue.auth.repository.entity;

import com.blue.basic.model.exps.BlueException;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * refresh token info entity
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "unused"})
public class RefreshInfo implements Serializable {

    private static final long serialVersionUID = -2732456864958923965L;

    /**
     * refresh keyId used for pk
     */
    @Id
    private String id;

    /**
     * random gamma
     */
    private String gamma;

    /**
     * member id
     */
    private String memberId;

    /**
     * credential type
     */
    private String credentialType;

    /**
     * device type
     */
    private String deviceType;

    /**
     * login time stamp
     */
    private String loginTime;

    /**
     * ttl
     */
    private Date expireAt;

    public RefreshInfo() {
    }

    public RefreshInfo(String id, String gamma, String memberId, String credentialType, String deviceType, String loginTime, Date expireAt) {
        if (isBlank(id))
            throw new BlueException(BAD_REQUEST);
        if (isBlank(gamma))
            throw new BlueException(BAD_REQUEST);
        if (isBlank(memberId))
            throw new BlueException(BAD_REQUEST);
        if (isBlank(credentialType))
            throw new BlueException(BAD_REQUEST);
        if (isBlank(deviceType))
            throw new BlueException(BAD_REQUEST);
        if (isBlank(loginTime))
            throw new BlueException(BAD_REQUEST);
        if (isNull(expireAt))
            throw new BlueException(BAD_REQUEST);

        this.id = id;
        this.gamma = gamma;
        this.memberId = memberId;
        this.credentialType = credentialType;
        this.deviceType = deviceType;
        this.loginTime = loginTime;
        this.expireAt = expireAt;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGamma() {
        return gamma;
    }

    public void setGamma(String gamma) {
        this.gamma = gamma;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getCredentialType() {
        return credentialType;
    }

    public void setCredentialType(String credentialType) {
        this.credentialType = credentialType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public Date getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Date expireAt) {
        this.expireAt = expireAt;
    }

    @Override
    public String toString() {
        return "RefreshInfo{" +
                "id='" + id + '\'' +
                ", gamma='" + gamma + '\'' +
                ", memberId='" + memberId + '\'' +
                ", credentialType='" + credentialType + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", loginTime='" + loginTime + '\'' +
                ", expireAt=" + expireAt +
                '}';
    }

}
