package com.blue.base.model.base;

import java.io.Serializable;

/**
 * 认证信息封装类
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class Access implements Serializable {

    private static final long serialVersionUID = -598996165142888324L;

    /**
     * 认证成员主键
     */
    private long id;

    /**
     * 认证用户角色
     */
    private long roleId;

    /**
     * 登录方式
     */
    private String loginType;

    /**
     * 设备类型
     */
    private String deviceType;

    /**
     * 本次登录时间戳/秒
     */
    private long loginTime;

    /**
     * 仅提供用于序列化,不推荐使用,基于set无法保证参数正确性
     */
    @Deprecated
    public Access() {
    }

    public Access(long id, long roleId, String loginType, String deviceType, long loginTime) {
        this.id = id;
        this.roleId = roleId;
        this.loginType = loginType;
        this.deviceType = deviceType;
        this.loginTime = loginTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public long getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(long loginTime) {
        this.loginTime = loginTime;
    }

    @Override
    public String toString() {
        return "Access{" +
                "id=" + id +
                ", roleId=" + roleId +
                ", loginType='" + loginType + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", loginTime='" + loginTime + '\'' +
                '}';
    }

}
