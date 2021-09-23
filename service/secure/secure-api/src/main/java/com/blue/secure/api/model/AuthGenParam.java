package com.blue.secure.api.model;

/**
 * 用户和角色信息封装
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class AuthGenParam {

    /**
     * 成员信息
     */
    private final Long memberId;

    /**
     * 角色信息
     */
    private final Long roleId;

    /**
     * 登录方式
     */
    private final String loginType;

    /**
     * 设备类型
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
