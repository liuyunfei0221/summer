package com.blue.finance.model.db;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.basic.common.base.BlueChecker.isNotNull;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.common.base.ConstantProcessor.assertOrderArticleStatus;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;
import static com.blue.finance.api.common.OrderArticleStatusChangeAsserter.assertStatusChange;

/**
 * order article update model
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class OrderArticleUpdateModel implements Serializable, Asserter {

    private static final long serialVersionUID = 1878911568644865041L;

    private Long id;

    private String extra;

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

    private Long updateTime;

    public OrderArticleUpdateModel() {
    }

    public OrderArticleUpdateModel(Long id, String extra, Integer originalStatus, Integer destStatus, Long updateTime) {
        this.id = id;
        this.extra = extra;
        this.originalStatus = originalStatus;
        this.destStatus = destStatus;
        this.updateTime = updateTime;
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(this.id))
            throw new BlueException(INVALID_IDENTITY);
        assertOrderArticleStatus(this.originalStatus, false);

        if (isNotNull(this.destStatus))
            assertStatusChange(this.originalStatus, this.destStatus);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
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

    public Long getUpdateTime() {
        return isNotNull(this.updateTime) ? this.updateTime : TIME_STAMP_GETTER.get();
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = isNotNull(updateTime) ? updateTime : TIME_STAMP_GETTER.get();
    }

    @Override
    public String toString() {
        return "OrderArticleUpdateModel{" +
                "id=" + id +
                ", extra='" + extra + '\'' +
                ", originalStatus=" + originalStatus +
                ", destStatus=" + destStatus +
                ", updateTime=" + updateTime +
                '}';
    }

}
