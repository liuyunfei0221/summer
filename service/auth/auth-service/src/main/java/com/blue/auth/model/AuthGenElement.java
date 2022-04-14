package com.blue.auth.model;

import com.blue.base.model.exps.BlueException;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;

/**
 * member, role, login infos for generate member auth
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class AuthGenElement {

    /**
     * member id
     */
    private final Long memberId;

    /**
     * role id
     */
    private final Long roleId;

    /**
     * credential type
     */
    private final String credentialType;

    /**
     * device type
     */
    private final String deviceType;


    public AuthGenElement(Long memberId, Long roleId, String credentialType, String deviceType) {
        if (memberId == null || memberId < 0L)
            throw new BlueException(BAD_REQUEST);
        if (roleId == null || roleId < 1L)
            throw new BlueException(BAD_REQUEST);
        if (credentialType == null || "".equals(credentialType))
            throw new BlueException(BAD_REQUEST);
        if (deviceType == null || "".equals(deviceType))
            throw new BlueException(BAD_REQUEST);

        this.memberId = memberId;
        this.roleId = roleId;
        this.credentialType = credentialType;
        this.deviceType = deviceType;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public String getCredentialType() {
        return credentialType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    @Override
    public String toString() {
        return "AuthGenParam{" +
                "memberId=" + memberId +
                ", roleId=" + roleId +
                ", credentialType='" + credentialType + '\'' +
                ", deviceType='" + deviceType + '\'' +
                '}';
    }

}
