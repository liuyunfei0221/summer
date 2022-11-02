package com.blue.finance.repository.entity;

import java.io.Serializable;

/**
 * order article entity, unit is fen
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public class OrderArticle implements Serializable {

    private static final long serialVersionUID = -464404199173506911L;

    private Long id;

    private Long orderId;

    private Long articleId;

    private Long memberId;

    private Long amount;

    private Long quantity;

    private String extra;

    private String detail;

    /**
     * order article status
     *
     * @see com.blue.basic.constant.finance.OrderArticleStatus
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
        return "OrderArticle{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", articleId=" + articleId +
                ", memberId=" + memberId +
                ", amount=" + amount +
                ", quantity=" + quantity +
                ", extra='" + extra + '\'' +
                ", detail='" + detail + '\'' +
                ", status=" + status +
                ", version=" + version +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

}