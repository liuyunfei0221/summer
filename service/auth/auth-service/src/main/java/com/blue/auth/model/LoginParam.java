package com.blue.auth.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.blue.base.common.base.ConstantProcessor.assertDeviceType;
import static com.blue.base.common.base.ConstantProcessor.assertLoginType;

/**
 * login infos
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused"})
public final class LoginParam implements Serializable {

    private static final long serialVersionUID = 1819262766642404008L;

    private static final int ENTRIES_SIZE = 4;
    private static final float LOAD_FACTOR = 1.0f;

    /**
     * login type
     *
     * @see com.blue.base.constant.auth.LoginType
     */
    private String loginType;

    /**
     * device type
     *
     * @see com.blue.base.constant.auth.DeviceType
     */
    private String deviceType;

    /**
     * diff params
     */
    private Map<String, String> account = new HashMap<>(ENTRIES_SIZE, LOAD_FACTOR);

    public LoginParam() {
    }

    public LoginParam(String loginType, String deviceType, Map<String, String> account) {
        this.loginType = loginType;
        assertLoginType(loginType, false);

        this.deviceType = deviceType;
        assertDeviceType(deviceType, false);

        this.account = account;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        assertLoginType(loginType, false);
        this.loginType = loginType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        assertDeviceType(deviceType, false);
        this.deviceType = deviceType;
    }

    public Map<String, String> getAccount() {
        return account;
    }

    public void setAccount(Map<String, String> params) {
        this.account = params;
    }

    public String getData(String key) {
        return account.get(key);
    }

    public String addData(String key, String value) {
        return account.put(key, value);
    }

    @Override
    public String toString() {
        return "LoginParam{" +
                "loginType='" + loginType + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", account=" + account +
                '}';
    }

}
