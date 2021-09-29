package com.blue.secure.api.model;

import com.blue.base.common.base.ConstantProcessor;
import com.blue.base.constant.base.BlueNumericalValue;
import com.blue.base.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;

/**
 * login infos
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class ClientLoginParam implements Serializable {

    private static final long serialVersionUID = 1819262766642404008L;

    private static final int ID_LEN_MAX = (int) BlueNumericalValue.ID_LEN_MAX.value;
    private static final int VFC_LEN_MAX = (int) BlueNumericalValue.VFC_LEN_MAX.value;
    private static final int ACS_LEN_MAX = (int) BlueNumericalValue.ACS_LEN_MAX.value;


    /**
     * account/phone/email for login
     */
    private String identity;

    /**
     * password/message verify
     */
    private String access;

    /**
     * verify
     */
    private String verificationCode;

    /**
     * login type
     */
    private String loginType;

    /**
     * device type
     */
    private String deviceType;

    public ClientLoginParam() {
    }

    public ClientLoginParam(String identity, String access, String verificationCode, String loginType, String deviceType) {
        if (identity == null || "".equals(identity))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "identity can't be blank");
        if (identity.length() > ID_LEN_MAX)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "identity too long");
        if (access == null || "".equals(access))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access can't be blank");
        if (access.length() > ACS_LEN_MAX)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access too long");

        if (verificationCode == null || "".equals(verificationCode))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "verificationCode can't be blank");
        if (access.length() > VFC_LEN_MAX)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "verificationCode too long");

        if (loginType == null || "".equals(loginType))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "loginType can't be blank");
        ConstantProcessor.assertLoginType(loginType);

        if (deviceType == null || "".equals(deviceType))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "deviceType can't be blank");
        ConstantProcessor.assertDeviceType(deviceType);

        this.identity = identity;
        this.access = access;
        this.verificationCode = verificationCode;
        this.loginType = loginType;
        this.deviceType = deviceType;
    }

    public String getIdentity() {
        if (identity == null || "".equals(identity))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "identity can't be blank");

        return identity;
    }

    public void setIdentity(String identity) {
        if (identity == null || "".equals(identity))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "identity can't be blank");
        if (identity.length() > ID_LEN_MAX)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "identity too long");

        this.identity = identity;
    }

    public String getAccess() {
        if (access == null || "".equals(access))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access can't be blank");

        return access;
    }

    public void setAccess(String access) {
        if (access == null || "".equals(access))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access can't be blank");
        if (access.length() > ACS_LEN_MAX)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access too long");

        this.access = access;
    }

    public String getVerificationCode() {
        if (verificationCode == null || "".equals(verificationCode))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "verificationCode can't be blank");

        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        if (verificationCode == null || "".equals(verificationCode))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "verificationCode can't be blank");
        if (access.length() > VFC_LEN_MAX)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "verificationCode too long");

        this.verificationCode = verificationCode;
    }

    public String getLoginType() {
        if (loginType == null || "".equals(loginType))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "loginType can't be blank");

        ConstantProcessor.assertLoginType(loginType);
        return loginType;
    }

    public void setLoginType(String loginType) {
        if (loginType == null || "".equals(loginType))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "loginType can't be blank");

        ConstantProcessor.assertLoginType(loginType);
        this.loginType = loginType;
    }

    public String getDeviceType() {
        if (deviceType == null || "".equals(deviceType))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "deviceType can't be blank");

        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        if (deviceType == null || "".equals(deviceType))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "deviceType can't be blank");

        ConstantProcessor.assertDeviceType(deviceType);
        this.deviceType = deviceType;
    }

}
