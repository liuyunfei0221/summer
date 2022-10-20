package com.blue.finance.api.model;

import java.io.Serializable;

/**
 * finance flow info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class FinanceFlowInfo implements Serializable {

    private static final long serialVersionUID = -7716489622474834740L;

    private Long id;

    private Long memberId;

    private Long orderId;

    private String orderNo;

    private String flowNo;

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

    private Long createTime;

    public FinanceFlowInfo() {
    }

    public FinanceFlowInfo(Long id, Long memberId, Long orderId, String orderNo, String flowNo, Integer type, Integer changeType, Long amountChanged, Long amountBeforeChanged, Long amountAfterChanged, Long createTime) {
        this.id = id;
        this.memberId = memberId;
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.flowNo = flowNo;
        this.type = type;
        this.changeType = changeType;
        this.amountChanged = amountChanged;
        this.amountBeforeChanged = amountBeforeChanged;
        this.amountAfterChanged = amountAfterChanged;
        this.createTime = createTime;
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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo;
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

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "FinanceFlowInfo{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", orderId=" + orderId +
                ", orderNo='" + orderNo + '\'' +
                ", flowNo='" + flowNo + '\'' +
                ", type=" + type +
                ", changeType=" + changeType +
                ", amountChanged=" + amountChanged +
                ", amountBeforeChanged=" + amountBeforeChanged +
                ", amountAfterChanged=" + amountAfterChanged +
                ", createTime=" + createTime +
                '}';
    }

}
