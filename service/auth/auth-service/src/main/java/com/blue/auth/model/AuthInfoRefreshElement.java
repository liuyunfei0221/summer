package com.blue.auth.model;

import com.blue.base.constant.auth.CredentialType;
import com.blue.base.constant.auth.DeviceType;

import java.io.Serializable;
import java.util.List;

/**
 * authInfo refresh params
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class AuthInfoRefreshElement implements Serializable {

    private static final long serialVersionUID = -8842596381325841676L;

    private Long memberId;
    private List<CredentialType> credentialTypes;
    private List<DeviceType> deviceTypes;

    private List<Long> roleIds;
    private String pubKey;

    public AuthInfoRefreshElement() {
    }

    public AuthInfoRefreshElement(Long memberId, List<CredentialType> credentialTypes, List<DeviceType> deviceTypes, List<Long> roleIds, String pubKey) {
        this.memberId = memberId;
        this.credentialTypes = credentialTypes;
        this.deviceTypes = deviceTypes;
        this.roleIds = roleIds;
        this.pubKey = pubKey;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public List<CredentialType> getCredentialTypes() {
        return credentialTypes;
    }

    public void setCredentialTypes(List<CredentialType> credentialTypes) {
        this.credentialTypes = credentialTypes;
    }

    public List<DeviceType> getDeviceTypes() {
        return deviceTypes;
    }

    public void setDeviceTypes(List<DeviceType> deviceTypes) {
        this.deviceTypes = deviceTypes;
    }

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public String getPubKey() {
        return pubKey;
    }

    public void setPubKey(String pubKey) {
        this.pubKey = pubKey;
    }

    @Override
    public String toString() {
        return "AuthInfoRefreshElement{" +
                "memberId=" + memberId +
                ", credentialTypes=" + credentialTypes +
                ", deviceTypes=" + deviceTypes +
                ", roleIds=" + roleIds +
                ", pubKey='" + pubKey + '\'' +
                '}';
    }

}
