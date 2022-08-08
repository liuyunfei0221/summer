package com.blue.basic.model.common;

import java.io.Serializable;
import java.util.List;

/**
 * access info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class Access implements Serializable {

    private static final long serialVersionUID = -598996165142888324L;

    /**
     * id
     */
    private long id;

    /**
     * role ids
     */
    private List<Long> roleIds;

    /**
     * credential type
     */
    private String credentialType;

    /**
     * device type
     */
    private String deviceType;

    /**
     * session timestamp(seconds)
     */
    private long loginTime;

    /**
     * It is only provided for serialization and is not recommended. The correctness of parameters cannot be guaranteed with setter
     */
    @Deprecated
    public Access() {
    }

    public Access(long id, List<Long> roleIds, String credentialType, String deviceType, long loginTime) {
        this.id = id;
        this.roleIds = roleIds;
        this.credentialType = credentialType;
        this.deviceType = deviceType;
        this.loginTime = loginTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public String getCredentialType() {
        return credentialType;
    }

    public void setCredentialType(String credentialType) {
        this.credentialType = credentialType;
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
                ", roleIds=" + roleIds +
                ", credentialType='" + credentialType + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", loginTime='" + loginTime + '\'' +
                '}';
    }

}
