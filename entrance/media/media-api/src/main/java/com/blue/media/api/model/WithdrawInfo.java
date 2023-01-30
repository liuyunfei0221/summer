package com.blue.media.api.model;

import com.blue.basic.serializer.Long2StringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * withdraw info for test
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class WithdrawInfo implements Serializable {

    private static final long serialVersionUID = 1417300777577441966L;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long amount;

    @JsonSerialize(using = Long2StringSerializer.class)
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