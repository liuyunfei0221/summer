package com.blue.auth.model;

import com.blue.base.constant.auth.CredentialType;

/**
 * credential setting up param
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused"})
public class CredentialSettingUpParam {

    /**
     * credential type: SV-SMS_VERIFY, PP-PHONE_PWD, EP-EMAIL_PWD, WE-WECHAT, MP-MINI_PRO, NLI-NOT_LOGGED_IN
     *
     * @see CredentialType
     */
    private String credentialType;

    private String credential;

    private String verificationCode;

    public CredentialSettingUpParam() {
    }

    public CredentialSettingUpParam(String credentialType, String credential, String verificationCode) {
        this.credentialType = credentialType;
        this.credential = credential;
        this.verificationCode = verificationCode;
    }

    public String getCredentialType() {
        return credentialType;
    }

    public void setCredentialType(String credentialType) {
        this.credentialType = credentialType;
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
                "credentialType='" + credentialType + '\'' +
                ", credential='" + credential + '\'' +
                ", verificationCode='" + verificationCode + '\'' +
                '}';
    }

}
