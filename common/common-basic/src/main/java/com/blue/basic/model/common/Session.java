package com.blue.basic.model.common;

import java.io.Serializable;
import java.util.List;

/**
 * session info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class Session implements Serializable {

    private static final long serialVersionUID = -4595028893083368393L;

    /**
     * id
     */
    private long id;

    /**
     * sec key / private key
     */
    private String secKey;

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
     * login timestamp(seconds)
     */
    private long loginTime;

    public Session(long id, String secKey, List<Long> roleIds, String credentialType, String deviceType, long loginTime) {
        this.id = id;
        this.secKey = secKey;
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

    public String getSecKey() {
        return secKey;
    }

    public void setSecKey(String secKey) {
        this.secKey = secKey;
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
        return "Session{" +
                "id=" + id +
                ", secKey='" + secKey + '\'' +
                ", roleIds=" + roleIds +
                ", credentialType='" + credentialType + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", loginTime=" + loginTime +
                '}';
    }

}