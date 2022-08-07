package com.blue.auth.model;

import com.blue.basic.constant.auth.CredentialType;
import com.blue.basic.inter.Asserter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.blue.basic.common.base.ConstantProcessor.assertCredentialType;
import static com.blue.basic.common.base.ConstantProcessor.assertDeviceType;

/**
 * login info
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public final class LoginParam implements Serializable, Asserter {

    private static final long serialVersionUID = 1819262766642404008L;

    private static final int ENTRIES_SIZE = 8;
    private static final float LOAD_FACTOR = 1.0f;

    /**
     * credential type
     *
     * @see CredentialType
     */
    private String credentialType;

    /**
     * device type
     *
     * @see com.blue.basic.constant.auth.DeviceType
     */
    private String deviceType;

    /**
     * diff params
     */
    private Map<String, String> account = new HashMap<>(ENTRIES_SIZE, LOAD_FACTOR);

    public LoginParam() {
    }

    public LoginParam(String credentialType, String deviceType, Map<String, String> account) {
        this.credentialType = credentialType;
        this.deviceType = deviceType;
        this.account = account;
    }

    @Override
    public void asserts() {
        assertCredentialType(this.credentialType, false);
        assertDeviceType(this.deviceType, false);
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
                "credentialType='" + credentialType + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", account=" + account +
                '}';
    }

}
