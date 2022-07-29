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

    private String extra;

    private String paymentExtra;

    private String detail;

    private Integer status;

    private Long version;

    private Long createTime;

    private Long updateTime;

    private Long paymentTime;

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
        this.orderNo = orderNo == null ? null : orderNo.trim();
    }

    public String getFlowNo() {
        return flowNo;
    }

    public void setFlowNo(String flowNo) {
        this.flowNo = flowNo == null ? null : flowNo.trim();
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

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra == null ? null : extra.trim();
    }

    public String getPaymentExtra() {
        return paymentExtra;
    }

    public void setPaymentExtra(String paymentExtra) {
        this.paymentExtra = paymentExtra == null ? null : paymentExtra.trim();
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
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

    public Long getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Long paymentTime) {
        this.paymentTime = paymentTime;
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
                ", extra='" + extra + '\'' +
                ", paymentExtra='" + paymentExtra + '\'' +
                ", detail='" + detail + '\'' +
                ", status=" + status +
                ", version=" + version +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", paymentTime=" + paymentTime +
                '}';
    }

}