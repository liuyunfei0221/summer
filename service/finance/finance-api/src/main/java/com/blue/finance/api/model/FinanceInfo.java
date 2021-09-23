package com.blue.finance.api.model;

import java.io.Serializable;

/**
 * 资金账户信息封装类
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class FinanceInfo implements Serializable {

    private static final long serialVersionUID = 2492781816830527690L;

    /**
     * 可用余额/分
     */
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
