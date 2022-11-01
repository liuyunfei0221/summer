package com.blue.finance.repository.entity;

import java.io.Serializable;

/**
 * order entity, unit is fen
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public class Order implements Serializable {

    private static final long serialVersionUID = -2787064934339551869L;

    private Long id;

    private Long memberId;

    private String orderNo;

    private String flowNo;

    private Integer type;

    private Integer paymentType;

    private Long amount;

    private Long payAmount;

    private Long paymentTime;

    private String extra;

    private String paymentExtra;

    private String detail;

    /**
     * order status
     *
     * @see com.blue.basic.constant.finance.OrderStatus
     */
    private Integer status;

    private Integer version;

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

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Long payAmount) {
        this.payAmount = payAmount;
    }

    public Long getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Long paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getPaymentExtra() {
        return paymentExtra;
    }

    public void setPaymentExtra(String paymentExtra) {
        this.paymentExtra = paymentExtra;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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
        return "Order{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", orderNo='" + orderNo + '\'' +
                ", flowNo='" + flowNo + '\'' +
                ", type=" + type +
                ", paymentType=" + paymentType +
                ", amount=" + amount +
                ", payAmount=" + payAmount +
                ", paymentTime=" + paymentTime +
                ", extra='" + extra + '\'' +
                ", paymentExtra='" + paymentExtra + '\'' +
                ", detail='" + detail + '\'' +
                ", status=" + status +
                ", version=" + version +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

}