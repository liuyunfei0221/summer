package com.blue.auth.model;

import java.io.Serializable;

/**
 * access update infos
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused"})
public final class AccessUpdateParam implements Serializable {

    private static final long serialVersionUID = 7921708039080732618L;

    /**
     * @see com.blue.base.constant.verify.VerifyType
     */
    private String verifyType;

    private String verificationCode;

    private String access;

    public AccessUpdateParam() {
    }

    public AccessUpdateParam(String verifyType, String verificationCode, String access) {
        this.verifyType = verifyType;
        this.verificationCode = verificationCode;
        this.access = access;
    }

    public String getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(String verifyType) {
        this.verifyType = verifyType;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    @Override
    public String toString() {
        return "AccessUpdateParam{" +
                "verifyType='" + verifyType + '\'' +
                ", verificationCode='" + verificationCode + '\'' +
                ", access='" + ":)" + '\'' +
                '}';
    }

}
