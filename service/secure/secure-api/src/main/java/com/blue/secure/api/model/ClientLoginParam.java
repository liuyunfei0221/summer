package com.blue.secure.api.model;

import com.blue.base.constant.base.BlueNumericalValue;
import com.blue.base.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.base.common.base.ConstantProcessor.assertDeviceType;
import static com.blue.base.common.base.ConstantProcessor.assertLoginType;
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
     *
     * @see com.blue.base.constant.secure.LoginType
     */
    private String loginType;

    /**
     * device type
     *
     * @see com.blue.base.constant.secure.DeviceType
     */
    private String deviceType;

    public ClientLoginParam() {
    }

    public ClientLoginParam(String identity, String access, String verificationCode, String loginType, String deviceType) {
        if (identity == null || "".equals(identity))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "identity can't be blank", null);
        if (identity.length() > ID_LEN_MAX)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "identity too long", null);
        if (access == null || "".equals(access))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access can't be blank", null);
        if (access.length() > ACS_LEN_MAX)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access too long", null);

        assertLoginType(loginType, false);

        assertDeviceType(deviceType, false);

        this.identity = identity;
        this.access = access;
        this.verificationCode = verificationCode;
        this.loginType = loginType;
        this.deviceType = deviceType;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        if (identity == null || "".equals(identity))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "identity can't be blank", null);
        if (identity.length() > ID_LEN_MAX)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "identity too long", null);

        this.identity = identity;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        if (access == null || "".equals(access))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access can't be blank", null);
        if (access.length() > ACS_LEN_MAX)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access too long", null);

        this.access = access;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
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

}
