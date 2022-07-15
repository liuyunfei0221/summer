package com.blue.auth.model;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static com.blue.basic.common.base.ConstantProcessor.assertVerifyType;
import static com.blue.basic.constant.auth.BlueAuthThreshold.*;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;

/**
 * access update info
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class AccessUpdateParam implements Serializable, Asserter {

    private static final long serialVersionUID = 7921708039080732618L;

    /**
     * @see com.blue.basic.constant.verify.VerifyType
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
        assertVerifyType(verifyType, false);

        int length = verificationCode.length();
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
