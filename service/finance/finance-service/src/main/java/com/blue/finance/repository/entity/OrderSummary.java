package com.blue.finance.repository.entity;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;
import java.util.List;

import static com.blue.basic.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;

/**
 * order summary in hbase
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public class OrderSummary implements Serializable, Asserter {

    private static final long serialVersionUID = 3234801680742676200L;

    /**
     * row key(order id)
     */
    private Long id;

    private Order order;

    List<OrderArticle> orderArticles;

    List<ReferenceAmount> referenceAmounts;

    private Integer version;

    public OrderSummary() {
    }

    public OrderSummary(Long id, Order order, List<OrderArticle> orderArticles, List<ReferenceAmount> referenceAmounts, Integer version) {
        this.id = id;
        this.order = order;
        this.orderArticles = orderArticles;
        this.referenceAmounts = referenceAmounts;
        this.version = version;
    }

    @Override
    public void asserts() {
        if (isInvalidIdentity(this.id))
            throw new BlueException(INVALID_IDENTITY);
        if (isNull(this.order))
            throw new BlueException(INVALID_PARAM);
        if (isNull(this.version) || this.version < 0)
            throw new BlueException(INVALID_PARAM);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderArticle> getOrderArticles() {
        return orderArticles;
    }

    public void setOrderArticles(List<OrderArticle> orderArticles) {
        this.orderArticles = orderArticles;
    }

    public List<ReferenceAmount> getReferenceAmounts() {
        return referenceAmounts;
    }

    public void setReferenceAmounts(List<ReferenceAmount> referenceAmounts) {
        this.referenceAmounts = referenceAmounts;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "OrderSummary{" +
                "id=" + id +
                ", order=" + order +
                ", orderArticles=" + orderArticles +
                ", referenceAmounts=" + referenceAmounts +
                ", version=" + version +
                '}';
    }

}
