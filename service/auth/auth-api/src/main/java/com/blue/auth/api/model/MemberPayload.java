package com.blue.auth.api.model;

import com.blue.base.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;

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
    private final String gamma;

    /**
     * auth keyId used for redis
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
     * login time stamp
     */
    private final String loginTime;

    public MemberPayload(String gamma, String keyId, String id, String credentialType, String deviceType, String loginTime) {
        if (gamma == null || "".equals(gamma))
            throw new BlueException(BAD_REQUEST);
        if (keyId == null || "".equals(keyId))
            throw new BlueException(BAD_REQUEST);
        if (id == null || "".equals(id))
            throw new BlueException(BAD_REQUEST);
        if (credentialType == null || "".equals(credentialType))
            throw new BlueException(BAD_REQUEST);
        if (deviceType == null || "".equals(deviceType))
            throw new BlueException(BAD_REQUEST);
        if (loginTime == null || "".equals(loginTime))
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
