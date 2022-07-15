package com.blue.auth.model;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static com.blue.basic.common.base.ConstantProcessor.assertVerifyType;
import static com.blue.basic.constant.auth.BlueAuthThreshold.*;
import static com.blue.basic.constant.auth.BlueAuthThreshold.ACS_LEN_MAX;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;

/**
 * access reset info
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
public final class AccessResetParam implements Serializable, Asserter {

    private static final long serialVersionUID = 2293803879668176107L;

    /**
     * @see com.blue.basic.constant.verify.VerifyType
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

    @Override
    public void asserts() {
        if (isBlank(verifyType) || isBlank(credential) || isBlank(verificationCode) || isBlank(access))
            throw new BlueException(EMPTY_PARAM);
        assertVerifyType(verifyType, false);

        int length = credential.length();
        if (length < CREDENTIAL_LEN_MIN.value || length > CREDENTIAL_LEN_MAX.value)
            throw new BlueException(INVALID_PARAM);

        length = verificationCode.length();
        if (length < VFC_LEN_MIN.value || length > VFC_LEN_MAX.value)
            throw new BlueException(INVALID_PARAM);

        length = access.length();
        if (length < ACS_LEN_MIN.value || length > ACS_LEN_MAX.value)
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
