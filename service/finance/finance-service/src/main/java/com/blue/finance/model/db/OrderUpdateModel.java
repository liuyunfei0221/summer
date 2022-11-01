package com.blue.finance.model.db;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.ConstantProcessor.assertOrderStatus;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;
import static com.blue.finance.api.common.OrderStatusChangeAsserter.assertStatusChange;

/**
 * order update model
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public final class OrderUpdateModel implements Serializable, Asserter {

    private static final long serialVersionUID = 2997759670471706666L;

    private Long id;

    private Long paymentTime;

    private String extra;

    private String paymentExtra;

    /**
     * original order status
     *
     * @see com.blue.basic.constant.finance.OrderStatus
     */
    private Integer originalStatus;

    /**
     * dest order status
     *
     * @see com.blue.basic.constant.finance.OrderStatus
     */
    private Integer destStatus;

    private Integer originalVersion;

    private Integer destVersion;

    private Long updateTime;

    public OrderUpdateModel() {
    }

    public OrderUpdateModel(Long id, Long paymentTime, String extra, String paymentExtra, Integer originalStatus, Integer destStatus, Integer originalVersion, Integer destVersion, Long updateTime) {
        this.id = id;
        this.paymentTime = paymentTime;
        this.extra = extra;
        this.paymentExtra = paymentExtra;
        this.originalStatus = originalStatus;
        this.destStatus = destStatus;
        this.originalVersion = originalVersion;
        this.destVersion = destVersion;
        this.updateTime = updateTime;
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(this.id))
            throw new BlueException(INVALID_IDENTITY);
        assertOrderStatus(this.originalStatus, false);
        if (isNotNull(this.destStatus))
            assertStatusChange(this.originalStatus, this.destStatus);

        if (isNull(this.originalVersion) || this.originalVersion < 0)
            throw new BlueException(INVALID_PARAM);
        if (isNull(this.destVersion) || this.destVersion < 0)
            throw new BlueException(INVALID_PARAM);
        if (this.destVersion <= this.originalVersion)
            throw new BlueException(INVALID_PARAM);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Integer getOriginalStatus() {
        return originalStatus;
    }

    public void setOriginalStatus(Integer originalStatus) {
        this.originalStatus = originalStatus;
    }

    public Integer getDestStatus() {
        return destStatus;
    }

    public void setDestStatus(Integer destStatus) {
        this.destStatus = destStatus;
    }

    public Integer getOriginalVersion() {
        return originalVersion;
    }

    public void setOriginalVersion(Integer originalVersion) {
        this.originalVersion = originalVersion;
    }

    public Integer getDestVersion() {
        return destVersion;
    }

    public void setDestVersion(Integer destVersion) {
        this.destVersion = destVersion;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "OrderUpdateModel{" +
                "id=" + id +
                ", paymentTime=" + paymentTime +
                ", extra='" + extra + '\'' +
                ", paymentExtra='" + paymentExtra + '\'' +
                ", originalStatus=" + originalStatus +
                ", destStatus=" + destStatus +
                ", originalVersion=" + originalVersion +
                ", destVersion=" + destVersion +
                ", updateTime=" + updateTime +
                '}';
    }

}
