package com.blue.secure.api.model;

import com.blue.base.common.base.ConstantProcessor;
import com.blue.base.constant.base.ThresholdNumericalValue;
import com.blue.base.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;

/**
 * 客户端登录账号密码数据封装
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class ClientLoginParam implements Serializable {

    private static final long serialVersionUID = 1819262766642404008L;

    private static final int ID_LEN_MAX = (int) ThresholdNumericalValue.ID_LEN_MAX.value;
    private static final int VFC_LEN_MAX = (int) ThresholdNumericalValue.VFC_LEN_MAX.value;
    private static final int ACS_LEN_MAX = (int) ThresholdNumericalValue.ACS_LEN_MAX.value;


    /**
     * 登录账号/手机号/邮件地址
     */
    private String identity;

    /**
     * 密码/短信验证码
     */
    private String access;

    /**
     * 验证码
     */
    private String verificationCode;

    /**
     * 登录类型,见LoginType
     */
    private String loginType;

    /**
     * 设备类型,见DeviceType
     */
    private String deviceType;


    public ClientLoginParam() {
    }

    public ClientLoginParam(String identity, String access, String verificationCode, String loginType, String deviceType) {
        if (identity == null || "".equals(identity))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "identity不能为空或''");
        if (identity.length() > ID_LEN_MAX)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "identity过长");
        if (access == null || "".equals(access))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access不能为空或''");
        if (access.length() > ACS_LEN_MAX)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access过长");

        if (verificationCode == null || "".equals(verificationCode))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "verificationCode不能为空或''");
        if (access.length() > VFC_LEN_MAX)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "verificationCode过长");

        if (loginType == null || "".equals(loginType))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "loginType不能为空或''");
        ConstantProcessor.assertLoginType(loginType);

        if (deviceType == null || "".equals(deviceType))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "deviceType不能为空或''");
        ConstantProcessor.assertDeviceType(deviceType);

        this.identity = identity;
        this.access = access;
        this.verificationCode = verificationCode;
        this.loginType = loginType;
        this.deviceType = deviceType;
    }

    public String getIdentity() {
        if (identity == null || "".equals(identity))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "identity不能为空或''");

        return identity;
    }

    public void setIdentity(String identity) {
        if (identity == null || "".equals(identity))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "identity不能为空或''");
        if (identity.length() > ID_LEN_MAX)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "identity过长");

        this.identity = identity;
    }

    public String getAccess() {
        if (access == null || "".equals(access))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access不能为空或''");

        return access;
    }

    public void setAccess(String access) {
        if (access == null || "".equals(access))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access不能为空或''");
        if (access.length() > ACS_LEN_MAX)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access过长");

        this.access = access;
    }

    public String getVerificationCode() {
        if (verificationCode == null || "".equals(verificationCode))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "verificationCode不能为空或''");

        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        if (verificationCode == null || "".equals(verificationCode))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "verificationCode不能为空或''");
        if (access.length() > VFC_LEN_MAX)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "verificationCode过长");

        this.verificationCode = verificationCode;
    }

    public String getLoginType() {
        if (loginType == null || "".equals(loginType))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "loginType不能为空或''");

        ConstantProcessor.assertLoginType(loginType);
        return loginType;
    }

    public void setLoginType(String loginType) {
        if (loginType == null || "".equals(loginType))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "loginType不能为空或''");

        ConstantProcessor.assertLoginType(loginType);
        this.loginType = loginType;
    }

    public String getDeviceType() {
        if (deviceType == null || "".equals(deviceType))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "deviceType不能为空或''");

        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        if (deviceType == null || "".equals(deviceType))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "deviceType不能为空或''");

        ConstantProcessor.assertDeviceType(deviceType);
        this.deviceType = deviceType;
    }

}
