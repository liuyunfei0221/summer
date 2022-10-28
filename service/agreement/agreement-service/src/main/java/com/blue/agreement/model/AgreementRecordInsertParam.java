package com.blue.agreement.model;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;

/**
 * params for insert a new agreement record
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class AgreementRecordInsertParam implements Serializable, Asserter {

    private static final long serialVersionUID = 15008478386309529L;

    private Long agreementId;

    public AgreementRecordInsertParam() {
    }

    public AgreementRecordInsertParam(Long agreementId) {
        this.agreementId = agreementId;
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(this.agreementId))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "invalid agreementId");
    }

    public Long getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(Long agreementId) {
        this.agreementId = agreementId;
    }

    @Override
    public String toString() {
        return "AgreementRecordInsertParam{" +
                "agreementId=" + agreementId +
                '}';
    }

}
