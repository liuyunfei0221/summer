package com.blue.auth.api.model;

import com.blue.base.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;

/**
 * member payload in jwt
 *
 * @author DarkBlue
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
     * login type
     */
    private final String loginType;

    /**
     * device type
     */
    private final String deviceType;

    /**
     * login time stamp
     */
    private final String loginTime;

    public MemberPayload(String gamma, String keyId, String id, String loginType, String deviceType, String loginTime) {
        if (gamma == null || "".equals(gamma))
            throw new BlueException(BAD_REQUEST);
        if (keyId == null || "".equals(keyId))
            throw new BlueException(BAD_REQUEST);
        if (id == null || "".equals(id))
            throw new BlueException(BAD_REQUEST);
        if (loginType == null || "".equals(loginType))
            throw new BlueException(BAD_REQUEST);
        if (deviceType == null || "".equals(deviceType))
            throw new BlueException(BAD_REQUEST);
        if (loginTime == null || "".equals(loginTime))
            throw new BlueException(BAD_REQUEST);

        this.gamma = gamma;
        this.keyId = keyId;
        this.id = id;
        this.loginType = loginType;
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

    public String getLoginType() {
        return loginType;
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
                ", loginType='" + loginType + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", loginTime='" + loginTime + '\'' +
                '}';
    }

}
