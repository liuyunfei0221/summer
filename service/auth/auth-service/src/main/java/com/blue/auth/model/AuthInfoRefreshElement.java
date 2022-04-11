package com.blue.auth.model;

import com.blue.base.constant.auth.AccessInfoRefreshElementType;
import com.blue.base.constant.auth.CredentialType;
import com.blue.base.constant.auth.DeviceType;

import java.io.Serializable;
import java.util.List;

/**
 * authInfo refresh params
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class AuthInfoRefreshElement implements Serializable {

    private static final long serialVersionUID = -8842596381325841676L;

    private Long memberId;
    private List<CredentialType> credentialTypes;
    private List<DeviceType> deviceTypes;
    private AccessInfoRefreshElementType elementType;
    private String elementValue;

    public AuthInfoRefreshElement() {
    }

    public AuthInfoRefreshElement(Long memberId, List<CredentialType> credentialTypes, List<DeviceType> deviceTypes, AccessInfoRefreshElementType elementType, String elementValue) {
        this.memberId = memberId;
        this.credentialTypes = credentialTypes;
        this.deviceTypes = deviceTypes;
        this.elementType = elementType;
        this.elementValue = elementValue;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public List<CredentialType> getcredentialTypes() {
        return credentialTypes;
    }

    public void setcredentialTypes(List<CredentialType> credentialTypes) {
        this.credentialTypes = credentialTypes;
    }

    public List<DeviceType> getDeviceTypes() {
        return deviceTypes;
    }

    public void setDeviceTypes(List<DeviceType> deviceTypes) {
        this.deviceTypes = deviceTypes;
    }

    public AccessInfoRefreshElementType getElementType() {
        return elementType;
    }

    public void setElementType(AccessInfoRefreshElementType elementType) {
        this.elementType = elementType;
    }

    public String getElementValue() {
        return elementValue;
    }

    public void setElementValue(String elementValue) {
        this.elementValue = elementValue;
    }

    @Override
    public String toString() {
        return "AuthInfoRefreshParam{" +
                "memberId=" + memberId +
                ", credentialTypes=" + credentialTypes +
                ", deviceTypes=" + deviceTypes +
                ", elementType=" + elementType +
                ", elementValue='" + elementValue + '\'' +
                '}';
    }

}
