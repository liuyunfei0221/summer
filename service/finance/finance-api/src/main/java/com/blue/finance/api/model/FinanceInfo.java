package com.blue.finance.api.model;

import com.blue.basic.serializer.Long2StringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * finance info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class FinanceInfo implements Serializable {

    private static final long serialVersionUID = 2492781816830527690L;

    /**
     * balance/fen
     */
    @JsonSerialize(using = Long2StringSerializer.class)
    private Long balance;

    public FinanceInfo(Long balance) {
        this.balance = balance;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "FinanceVO{" +
                "balance=" + balance +
                '}';
    }

}
