package com.blue.secure.model;

import static com.blue.base.constant.base.CommonException.BAD_REQUEST_EXP;

/**
 * member, role, login infos for generate member auth
 *
 * @author DarkBlue
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
     * login type
     */
    private final String loginType;

    /**
     * device type
     */
    private final String deviceType;


    public AuthGenElement(Long memberId, Long roleId, String loginType, String deviceType) {
        if (memberId == null || memberId < 0L)
            throw BAD_REQUEST_EXP.exp;
        if (roleId == null || roleId < 1L)
            throw BAD_REQUEST_EXP.exp;
        if (loginType == null || "".equals(loginType))
            throw BAD_REQUEST_EXP.exp;
        if (deviceType == null || "".equals(deviceType))
            throw BAD_REQUEST_EXP.exp;

        this.memberId = memberId;
        this.roleId = roleId;
        this.loginType = loginType;
        this.deviceType = deviceType;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public String getLoginType() {
        return loginType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    @Override
    public String toString() {
        return "AuthGenParam{" +
                "memberId=" + memberId +
                ", roleId=" + roleId +
                ", loginType='" + loginType + '\'' +
                ", deviceType='" + deviceType + '\'' +
                '}';
    }

}
