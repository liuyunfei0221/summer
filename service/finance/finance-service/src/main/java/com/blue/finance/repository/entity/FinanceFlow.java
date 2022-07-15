package com.blue.finance.repository.entity;

import java.io.Serializable;

/**
 * finance flow entity, unit is fen
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public class FinanceFlow implements Serializable {

    private static final long serialVersionUID = -7927905031672744480L;

    private Long id;

    private Long memberId;

    private Long orderId;

    /**
     * @see com.blue.basic.constant.finance.FlowType
     */
    private Integer type;

    /**
     * @see com.blue.basic.constant.finance.FlowChangeType
     */
    private Integer changeType;

    private Long amountChanged;

    private Long amountBeforeChanged;

    private Long amountAfterChanged;

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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getChangeType() {
        return changeType;
    }

    public void setChangeType(Integer changeType) {
        this.changeType = changeType;
    }

    public Long getAmountChanged() {
        return amountChanged;
    }

    public void setAmountChanged(Long amountChanged) {
        this.amountChanged = amountChanged;
    }

    public Long getAmountBeforeChanged() {
        return amountBeforeChanged;
    }

    public void setAmountBeforeChanged(Long amountBeforeChanged) {
        this.amountBeforeChanged = amountBeforeChanged;
    }

    public Long getAmountAfterChanged() {
        return amountAfterChanged;
    }

    public void setAmountAfterChanged(Long amountAfterChanged) {
        this.amountAfterChanged = amountAfterChanged;
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
        return "FinanceFlow{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", orderId=" + orderId +
                ", type=" + type +
                ", changeType=" + changeType +
                ", amountChanged=" + amountChanged +
                ", amountBeforeChanged=" + amountBeforeChanged +
                ", amountAfterChanged=" + amountAfterChanged +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

}