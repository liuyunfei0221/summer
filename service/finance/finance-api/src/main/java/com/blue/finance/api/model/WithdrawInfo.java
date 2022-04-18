package com.blue.finance.api.model;

import com.blue.base.inter.Asserter;
import com.blue.base.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.base.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;

/**
 * withdraw info
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class WithdrawInfo implements Serializable, Asserter {

    private static final long serialVersionUID = 1417300777577441966L;

    /**
     * withdraw amount
     */
    private Long amount;

    /**
     * bankcard id
     */
    private Long bankCardId;

    /**
     * remark
     */
    private String remark;

    public WithdrawInfo() {
    }

    public WithdrawInfo(Long amount, Long bankCardId, String remark) {
        this.amount = amount;
        this.bankCardId = bankCardId;
        this.remark = remark;
    }

    @Override
    public void asserts() {
        if (isNull(amount) || amount < 0L || isInvalidIdentity(bankCardId))
            throw new BlueException(BAD_REQUEST);
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getBankCardId() {
        return bankCardId;
    }

    public void setBankCardId(Long bankCardId) {
        this.bankCardId = bankCardId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "WithdrawInfo{" +
                "amount=" + amount +
                ", bankCardId=" + bankCardId +
                ", remark='" + remark + '\'' +
                '}';
    }

}
