package com.blue.auth.api.model;

import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * member payload in jwt
 *
 * @author liuyunfei
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class MemberPayload implements Serializable {

    private static final long serialVersionUID = 2135297516865116214L;

    /**
     * random gamma
     */
    private String gamma;

    /**
     * auth keyId for redis
     */
    private final String keyId;

    /**
     * member id
     */
    private final String id;

    /**
     * credential type
     */
    private final String credentialType;

    /**
     * device type
     */
    private final String deviceType;

    /**
     * session time stamp
     */
    private final String loginTime;

    public MemberPayload(String gamma, String keyId, String id, String credentialType, String deviceType, String loginTime) {
        if (isBlank(gamma))
            throw new BlueException(BAD_REQUEST);
        if (isBlank(keyId))
            throw new BlueException(BAD_REQUEST);
        if (isBlank(id))
            throw new BlueException(BAD_REQUEST);
        if (isBlank(credentialType))
            throw new BlueException(BAD_REQUEST);
        if (isBlank(deviceType))
            throw new BlueException(BAD_REQUEST);
        if (isBlank(loginTime))
            throw new BlueException(BAD_REQUEST);

        this.gamma = gamma;
        this.keyId = keyId;
        this.id = id;
        this.credentialType = credentialType;
        this.deviceType = deviceType;
        this.loginTime = loginTime;
    }

    public String getGamma() {
        return gamma;
    }

    public void setGamma(String gamma) {
        if (isBlank(gamma))
            throw new BlueException(BAD_REQUEST);
        this.gamma = gamma;
    }

    public String getKeyId() {
        return keyId;
    }

    public String getId() {
        return id;
    }

    public String getCredentialType() {
        return credentialType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getLoginTime() {
        return loginTime;
    }

    @Override
    public String toString() {
        return "MemberPayload{" +
                "gamma='" + gamma + '\'' +
                ", keyId='" + keyId + '\'' +
                ", id='" + id + '\'' +
                ", credentialType='" + credentialType + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", loginTime='" + loginTime + '\'' +
                '}';
    }

}
