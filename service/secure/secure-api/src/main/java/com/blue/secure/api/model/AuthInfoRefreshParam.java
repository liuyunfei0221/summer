package com.blue.secure.api.model;

import com.blue.base.constant.secure.AuthInfoRefreshElementType;
import com.blue.base.constant.secure.DeviceType;
import com.blue.base.constant.secure.LoginType;

import java.io.Serializable;
import java.util.List;

/**
 * authInfo属性动态刷新参数封装
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class AuthInfoRefreshParam implements Serializable {

    private static final long serialVersionUID = -8842596381325841676L;

    private Long memberId;
    private List<LoginType> loginTypes;
    private List<DeviceType> deviceTypes;
    private AuthInfoRefreshElementType elementType;
    private String elementValue;

    public AuthInfoRefreshParam() {
    }

    public AuthInfoRefreshParam(Long memberId, List<LoginType> loginTypes, List<DeviceType> deviceTypes, AuthInfoRefreshElementType elementType, String elementValue) {
        this.memberId = memberId;
        this.loginTypes = loginTypes;
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

    public List<LoginType> getLoginTypes() {
        return loginTypes;
    }

    public void setLoginTypes(List<LoginType> loginTypes) {
        this.loginTypes = loginTypes;
    }

    public List<DeviceType> getDeviceTypes() {
        return deviceTypes;
    }

    public void setDeviceTypes(List<DeviceType> deviceTypes) {
        this.deviceTypes = deviceTypes;
    }

    public AuthInfoRefreshElementType getElementType() {
        return elementType;
    }

    public void setElementType(AuthInfoRefreshElementType elementType) {
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
                ", loginTypes=" + loginTypes +
                ", deviceTypes=" + deviceTypes +
                ", elementType=" + elementType +
                ", elementValue='" + elementValue + '\'' +
                '}';
    }

}
