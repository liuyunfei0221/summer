package com.blue.auth.model;

import com.blue.basic.model.exps.BlueException;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;

/**
 * member, role, session info for generate member auth
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
        if (isNull(memberId) || memberId < 0L)
            throw new BlueException(BAD_REQUEST);
        if (isNull(roleId) || roleId < 1L)
            throw new BlueException(BAD_REQUEST);
        if (isNull(credentialType) || EMPTY_VALUE.value.equals(credentialType))
            throw new BlueException(BAD_REQUEST);
        if (isNull(deviceType) || EMPTY_VALUE.value.equals(deviceType))
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
