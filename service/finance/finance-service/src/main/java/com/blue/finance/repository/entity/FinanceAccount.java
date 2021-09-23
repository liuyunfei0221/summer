package com.blue.finance.repository.entity;

/**
 * 成员资金账户表
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class FinanceAccount {

    /**
     * 主键
     */
    private Long id;

    /**
     * 成员id
     */
    private Long memberId;

    /**
     * 可用余额/分
     */
    private Long balance;

    /**
     * 冻结金额/分
     */
    private Long frozen;

    /**
     * 历史总收入/分
     */
    private Long income;

    /**
     * 历史总支出/分
     */
    private Long outlay;

    /**
     * 状态 0停用 1启用
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Long createTime;

    /**
     * 修改时间
     */
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