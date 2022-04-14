package com.blue.auth.model;

import com.blue.base.inter.Asserter;
import com.blue.base.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;

/**
 * access reset infos
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class AccessResetParam implements Serializable, Asserter {

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

    @Override
    public void asserts() {
        if (isBlank(verifyType) || isBlank(credential) || isBlank(verificationCode) || isBlank(access))
            throw new BlueException(EMPTY_PARAM);
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
