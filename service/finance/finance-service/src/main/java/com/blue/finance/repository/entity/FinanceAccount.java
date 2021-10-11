package com.blue.finance.repository.entity;

/**
 * finance account/unit is fen
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class FinanceAccount {

    private Long id;

    private Long memberId;

    private Long balance;

    private Long frozen;

    private Long income;

    private Long outlay;

    private Integer status;

    private Long createTime;

    private Long updateTime;


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


    public Long getCreateTime() {
        return createTime;
    }


    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }


    public Long getUpdateTime() {
        return updateTime;
    }


    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "FinanceAccount{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", balance=" + balance +
                ", frozen=" + frozen +
                ", income=" + income +
                ", outlay=" + outlay +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

}