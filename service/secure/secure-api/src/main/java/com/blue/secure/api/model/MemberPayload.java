package com.blue.secure.api.model;

import java.io.Serializable;

/**
 * token中需要存放的用户信息
 *
 * @author DarkBlue
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
public final class MemberPayload implements Serializable {

    private static final long serialVersionUID = 2135297516865116214L;

    /**
     * 随机信息
     */
    private final String gamma;

    /**
     * 对应缓存key
     */
    private final String keyId;

    /**
     * 成员id
     */
    private final String id;

    /**
     * 登录类型
     */
    private final String loginType;

    /**
     * 设备类型
     */
    private final String deviceType;

    /**
     * 本次登录时间戳/秒
     */
    private final String loginTime;

    public MemberPayload(String gamma, String keyId, String id, String loginType, String deviceType, String loginTime) {
        if (gamma == null || "".equals(gamma))
            throw new RuntimeException("gamma不能为空或''");
        if (keyId == null || "".equals(keyId))
            throw new RuntimeException("keyId不能为空或''");
        if (id == null || "".equals(id))
            throw new RuntimeException("id不能为空或''");
        if (loginType == null || "".equals(loginType))
            throw new RuntimeException("loginType不能为空或''");
        if (deviceType == null || "".equals(deviceType))
            throw new RuntimeException("deviceType不能为空或''");
        if (loginTime == null || "".equals(loginTime))
            throw new RuntimeException("loginTime不能为空或''");

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
