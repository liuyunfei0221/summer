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
 * credential modify param
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class CredentialModifyParam implements Serializable, Asserter {

    private static final long serialVersionUID = 2767548737718964659L;

    /**
     * @see com.blue.basic.constant.verify.VerifyType
     */
    private String currentVerifyType;

    private String currentVerificationCode;

    /**
     * @see com.blue.basic.constant.verify.VerifyType
     */
    private String destinationVerifyType;

    /**
     * phone/email ...
     */
    private String destinationCredential;

    private String destinationVerificationCode;

    public CredentialModifyParam() {
    }

    public CredentialModifyParam(String currentVerifyType, String currentVerificationCode, String destinationVerifyType, String destinationCredential, String destinationVerificationCode) {
        this.currentVerifyType = currentVerifyType;
        this.currentVerificationCode = currentVerificationCode;
        this.destinationVerifyType = destinationVerifyType;
        this.destinationCredential = destinationCredential;
        this.destinationVerificationCode = destinationVerificationCode;
    }

    @Override
    public void asserts() {
        if (isBlank(currentVerifyType) || isBlank(currentVerificationCode) || isBlank(destinationVerifyType) || isBlank(destinationVerificationCode) || isBlank(destinationCredential))
            throw new BlueException(EMPTY_PARAM);
        assertVerifyType(currentVerifyType, false);
        assertVerifyType(destinationVerifyType, false);

        int length = currentVerificationCode.length();
        if (length < VFC_LEN_MIN.value || length > VFC_LEN_MAX.value)
            throw new BlueException(INVALID_PARAM);

        length = destinationCredential.length();
        if (length < CREDENTIAL_LEN_MIN.value || length > CREDENTIAL_LEN_MAX.value)
            throw new BlueException(INVALID_PARAM);

        length = destinationVerificationCode.length();
        if (length < VFC_LEN_MIN.value || length > VFC_LEN_MAX.value)
            throw new BlueException(INVALID_PARAM);
    }

    public String getCurrentVerifyType() {
        return currentVerifyType;
    }

    public void setCurrentVerifyType(String currentVerifyType) {
        this.currentVerifyType = currentVerifyType;
    }

    public String getCurrentVerificationCode() {
        return currentVerificationCode;
    }

    public void setCurrentVerificationCode(String currentVerificationCode) {
        this.currentVerificationCode = currentVerificationCode;
    }

    public String getDestinationVerifyType() {
        return destinationVerifyType;
    }

    public void setDestinationVerifyType(String destinationVerifyType) {
        this.destinationVerifyType = destinationVerifyType;
    }

    public String getDestinationCredential() {
        return destinationCredential;
    }

    public void setDestinationCredential(String destinationCredential) {
        this.destinationCredential = destinationCredential;
    }

    public String getDestinationVerificationCode() {
        return destinationVerificationCode;
    }

    public void setDestinationVerificationCode(String destinationVerificationCode) {
        this.destinationVerificationCode = destinationVerificationCode;
    }

    @Override
    public String toString() {
        return "CredentialModifyParam{" +
                "currentVerifyType='" + currentVerifyType + '\'' +
                ", currentVerificationCode='" + currentVerificationCode + '\'' +
                ", destinationVerifyType='" + destinationVerifyType + '\'' +
                ", destinationCredential='" + destinationCredential + '\'' +
                ", destinationVerificationCode='" + destinationVerificationCode + '\'' +
                '}';
    }

}
