package com.blue.file.api.model;

import java.io.Serializable;

/**
 * withdraw info for test
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class WithdrawInfo implements Serializable {

    private static final long serialVersionUID = 1417300777577441966L;

    private Long amount;

    private Long bankCardId;

    private String remark;

    public WithdrawInfo() {
    }

    public WithdrawInfo(Long amount, Long bankCardId, String remark) {
        this.amount = amount;
        this.bankCardId = bankCardId;
        this.remark = remark;
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
