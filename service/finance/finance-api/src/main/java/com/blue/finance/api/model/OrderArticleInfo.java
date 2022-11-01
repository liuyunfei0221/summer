package com.blue.finance.api.model;

import com.blue.basic.serializer.Long2StringSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.Serializable;

/**
 * order article info
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class OrderArticleInfo implements Serializable {

    private static final long serialVersionUID = 1627470241391413546L;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long id;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long orderId;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long articleId;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long memberId;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long amount;

    @JsonSerialize(using = Long2StringSerializer.class)
    private Long quantity;

    private String extra;

    private String detail;

    /**
     * order article status
     *
     * @see com.blue.basic.constant.finance.OrderArticleStatus
     */
    private Integer status;

    public OrderArticleInfo() {
    }

    public OrderArticleInfo(Long id, Long orderId, Long articleId, Long memberId, Long amount, Long quantity, String extra, String detail, Integer status) {
        this.id = id;
        this.orderId = orderId;
        this.articleId = articleId;
        this.memberId = memberId;
        this.amount = amount;
        this.quantity = quantity;
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

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
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
        return "OrderArticleInfo{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", articleId=" + articleId +
                ", memberId=" + memberId +
                ", amount=" + amount +
                ", quantity=" + quantity +
                ", extra='" + extra + '\'' +
                ", detail='" + detail + '\'' +
                ", status=" + status +
                '}';
    }

}
