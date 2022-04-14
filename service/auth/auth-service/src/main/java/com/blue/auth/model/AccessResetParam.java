package com.blue.auth.model;

import java.io.Serializable;

/**
 * access reset infos
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused"})
public final class AccessResetParam implements Serializable {

    private static final long serialVersionUID = 2293803879668176107L;

    /**
     * @see com.blue.base.constant.verify.VerifyType
     */
    private String verifyType;

    /**
     * phone/email ...
     */
    private String credential;

    private String verificationCode;

    private String access;

    public AccessResetParam() {
    }

    public AccessResetParam(String verifyType, String credential, String verificationCode, String access) {
        this.verifyType = verifyType;
        this.credential = credential;
        this.verificationCode = verificationCode;
        this.access = access;
    }

    public String getVerifyType() {
        return verifyType;
    }

    public void setVerifyType(String verifyType) {
        this.verifyType = verifyType;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
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
        return "AccessResetParam{" +
                "verifyType='" + verifyType + '\'' +
                ", credential='" + credential + '\'' +
                ", verificationCode='" + verificationCode + '\'' +
                ", access='" + ":)" + '\'' +
                '}';
    }

}
