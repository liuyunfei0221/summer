package com.blue.auth.model;

import com.blue.base.inter.Asserter;
import com.blue.base.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.common.base.ConstantProcessor.assertVerifyType;
import static com.blue.base.constant.auth.BlueAuthThreshold.*;
import static com.blue.base.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.common.ResponseElement.INVALID_PARAM;

/**
 * credential setting up param
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class CredentialSettingUpParam implements Serializable, Asserter {

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

    @Override
    public void asserts() {
        if (isBlank(verifyType) || isBlank(credential) || isBlank(verificationCode))
            throw new BlueException(EMPTY_PARAM);
        assertVerifyType(verifyType, false);

        int length = credential.length();
        if (length < CREDENTIAL_LEN_MIN.value || length > CREDENTIAL_LEN_MAX.value)
            throw new BlueException(INVALID_PARAM);

        length = verificationCode.length();
        if (length < VFC_LEN_MIN.value || length > VFC_LEN_MAX.value)
            throw new BlueException(INVALID_PARAM);
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
