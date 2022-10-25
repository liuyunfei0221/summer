package com.blue.finance.api.model;

import com.blue.basic.serializer.Long2StringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * finance account info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public class FinanceAccountInfo implements Serializable {

    private static final long serialVersionUID = -6369208980343449797L;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long id;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long memberId;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long balance;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long frozen;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long income;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long outlay;

    private Integer status;

    public FinanceAccountInfo() {
    }

    public FinanceAccountInfo(Long id, Long memberId, Long balance, Long frozen, Long income, Long outlay, Integer status) {
        this.id = id;
        this.memberId = memberId;
        this.balance = balance;
        this.frozen = frozen;
        this.income = income;
        this.outlay = outlay;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public Long getFrozen() {
        return frozen;
    }

    public void setFrozen(Long frozen) {
        this.frozen = frozen;
    }

    public Long getIncome() {
        return income;
    }

    public void setIncome(Long income) {
        this.income = income;
    }

    public Long getOutlay() {
        return outlay;
    }

    public void setOutlay(Long outlay) {
        this.outlay = outlay;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "FinanceAccountInfo{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", balance=" + balance +
                ", frozen=" + frozen +
                ", income=" + income +
                ", outlay=" + outlay +
                ", status=" + status +
                '}';
    }

}
