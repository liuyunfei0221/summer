package com.blue.secure.api.model;

import java.io.Serializable;

import static com.blue.base.constant.base.CommonException.BAD_REQUEST_EXP;

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
            throw BAD_REQUEST_EXP.exp;
        if (keyId == null || "".equals(keyId))
            throw BAD_REQUEST_EXP.exp;
        if (id == null || "".equals(id))
            throw BAD_REQUEST_EXP.exp;
        if (loginType == null || "".equals(loginType))
            throw BAD_REQUEST_EXP.exp;
        if (deviceType == null || "".equals(deviceType))
            throw BAD_REQUEST_EXP.exp;
        if (loginTime == null || "".equals(loginTime))
            throw BAD_REQUEST_EXP.exp;

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
