package com.blue.finance.api.model;

import com.blue.basic.serializer.Long2StringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * order info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class OrderInfo implements Serializable {

    private static final long serialVersionUID = -560325284957024821L;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long id;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long memberId;

    private String orderNo;

    private String flowNo;

    private Integer type;

    private Integer paymentType;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long amount;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long payAmount;

    private String extra;

    private String paymentExtra;

    private String detail;

    private Integer status;

    public OrderInfo() {
    }

    public OrderInfo(Long id, Long memberId, String orderNo, String flowNo, Integer type, Integer paymentType, Long amount, Long payAmount, String extra, String paymentExtra, String detail, Integer status) {
        this.id = id;
        this.memberId = memberId;
        this.orderNo = orderNo;
        this.flowNo = flowNo;
        this.type = type;
        this.paymentType = paymentType;
        this.amount = amount;
        this.payAmount = payAmount;
        this.extra = extra;
        this.paymentExtra = paymentExtra;
        this.detail = detail;
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

    @Override
    public String toString() {
        return "OrderInfo{" +
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
                '}';
    }

}
