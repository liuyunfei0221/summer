package com.blue.base.model.base;

import java.io.Serializable;

/**
 * access info
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class Access implements Serializable {

    private static final long serialVersionUID = -598996165142888324L;

    /**
     * id
     */
    private long id;

    /**
     * role id
     */
    private long roleId;

    /**
     * login type
     */
    private String loginType;

    /**
     * device type
     */
    private String deviceType;

    /**
     * login timestamp(seconds)
     */
    private long loginTime;

    /**
     * It is only provided for serialization and is not recommended. The correctness of parameters cannot be guaranteed with setter
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
