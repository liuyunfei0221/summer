package com.blue.secure.api.model;

/**
 * member, role, login infos for generate member auth
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class AuthGenParam {

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


    public AuthGenParam(Long memberId, Long roleId, String loginType, String deviceType) {
        if (memberId == null || memberId < 1L)
            throw new RuntimeException("memberId不能为空或小于1");
        if (roleId == null || roleId < 1L)
            throw new RuntimeException("roleId不能为空或小于1");
        if (loginType == null || "".equals(loginType))
            throw new RuntimeException("loginType不能为空");
        if (deviceType == null || "".equals(deviceType))
            throw new RuntimeException("deviceType不能为空");

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
