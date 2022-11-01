package com.blue.finance.api.model;

import com.blue.basic.serializer.Long2StringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * reference amount info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class ReferenceAmountInfo implements Serializable {

    private static final long serialVersionUID = 7184571414765863906L;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long id;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long orderId;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long memberId;

    private Integer type;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long referenceId;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long amount;

    private String extra;

    private String detail;

    /**
     * order article status
     *
     * @see com.blue.basic.constant.finance.OrderArticleStatus
     */
    private Integer status;

    public ReferenceAmountInfo() {
    }

    public ReferenceAmountInfo(Long id, Long orderId, Long memberId, Integer type, Long referenceId, Long amount, String extra, String detail, Integer status) {
        this.id = id;
        this.orderId = orderId;
        this.memberId = memberId;
        this.type = type;
        this.referenceId = referenceId;
        this.amount = amount;
        this.extra = extra;
        this.detail = detail;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
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
        return "ReferenceAmountInfo{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", memberId=" + memberId +
                ", type=" + type +
                ", referenceId=" + referenceId +
                ", amount=" + amount +
                ", extra='" + extra + '\'' +
                ", detail='" + detail + '\'' +
                ", status=" + status +
                '}';
    }

}
