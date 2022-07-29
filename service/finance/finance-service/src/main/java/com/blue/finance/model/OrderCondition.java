package com.blue.finance.model;

import com.blue.basic.constant.common.SortType;
import com.blue.basic.model.common.SortCondition;
import com.blue.finance.constant.FinanceFlowSortAttribute;

import java.io.Serializable;

/**
 * finance flow condition for select
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class OrderCondition extends SortCondition implements Serializable {

    private static final long serialVersionUID = -5014270861523252267L;

    private Long id;

    private Long memberId;

    private Long orderId;

    private String orderNo;

    /**
     * @see com.blue.basic.constant.finance.FlowType
     */
    private Integer type;

    /**
     * @see com.blue.basic.constant.finance.FlowChangeType
     */
    private Integer changeType;

    private Long amountChangedMin;

    private Long amountChangedMax;

    private Long createTimeBegin;

    private Long createTimeEnd;

    public OrderCondition() {
        super(FinanceFlowSortAttribute.ID.attribute, SortType.DESC.identity);
    }

    public OrderCondition(String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
    }

    public OrderCondition(Long id, Long memberId, Long orderId, String orderNo, Integer type, Integer changeType, Long amountChangedMin, Long amountChangedMax,
                          Long createTimeBegin, Long createTimeEnd, String sortAttribute, String sortType) {
        super(sortAttribute, sortType);
        this.id = id;
        this.memberId = memberId;
        this.orderId = orderId;
        this.orderNo = orderNo;
        this.type = type;
        this.changeType = changeType;
        this.amountChangedMin = amountChangedMin;
        this.amountChangedMax = amountChangedMax;
        this.createTimeBegin = createTimeBegin;
        this.createTimeEnd = createTimeEnd;
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

    public Long getAmountChangedMin() {
        return amountChangedMin;
    }

    public void setAmountChangedMin(Long amountChangedMin) {
        this.amountChangedMin = amountChangedMin;
    }

    public Long getAmountChangedMax() {
        return amountChangedMax;
    }

    public void setAmountChangedMax(Long amountChangedMax) {
        this.amountChangedMax = amountChangedMax;
    }

    public Long getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(Long createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public Long getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Long createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    @Override
    public String toString() {
        return "FinanceFlowCondition{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", orderId=" + orderId +
                ", orderNo='" + orderNo + '\'' +
                ", type=" + type +
                ", changeType=" + changeType +
                ", amountChangedMin=" + amountChangedMin +
                ", amountChangedMax=" + amountChangedMax +
                ", createTimeBegin=" + createTimeBegin +
                ", createTimeEnd=" + createTimeEnd +
                ", sortAttribute='" + sortAttribute + '\'' +
                ", sortType='" + sortType + '\'' +
                '}';
    }

}
