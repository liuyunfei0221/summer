package com.blue.auth.model;

import com.blue.base.inter.Asserter;
import com.blue.base.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;

/**
 * access update infos
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class AccessUpdateParam implements Serializable, Asserter {

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

    @Override
    public void asserts() {
        if (isBlank(verifyType) || isBlank(verificationCode) || isBlank(access))
            throw new BlueException(EMPTY_PARAM);
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
