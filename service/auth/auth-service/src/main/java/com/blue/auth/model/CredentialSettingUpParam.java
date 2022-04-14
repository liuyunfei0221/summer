package com.blue.auth.model;

import java.io.Serializable;

/**
 * credential setting up param
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused"})
public class CredentialSettingUpParam implements Serializable {

    private static final long serialVersionUID = -4028585836242187864L;

    /**
     * @see com.blue.base.constant.verify.VerifyType
     */
    private String verifyType;

    /**
     * phone/email ...
     */
    private String credential;

    private String verificationCode;

    public CredentialSettingUpParam() {
    }

    public CredentialSettingUpParam(String verifyType, String credential, String verificationCode) {
        this.verifyType = verifyType;
        this.credential = credential;
        this.verificationCode = verificationCode;
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

    @Override
    public String toString() {
        return "CredentialSettingUpParam{" +
                "verifyType='" + verifyType + '\'' +
                ", credential='" + credential + '\'' +
                ", verificationCode='" + verificationCode + '\'' +
                '}';
    }

}
