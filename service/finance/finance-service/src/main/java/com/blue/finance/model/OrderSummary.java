package com.blue.finance.model;

import com.blue.finance.repository.entity.Order;
import com.blue.finance.repository.entity.OrderArticle;
import com.blue.finance.repository.entity.ReferenceAmount;

import java.io.Serializable;
import java.util.List;

/**
 * order summary
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public class OrderSummary implements Serializable {

    private static final long serialVersionUID = 3234801680742676200L;

    private Order order;

    List<OrderArticle> orderArticles;

    List<ReferenceAmount> referenceAmounts;

    private Long version;

    public OrderSummary() {
    }

    public OrderSummary(Order order, List<OrderArticle> orderArticles, List<ReferenceAmount> referenceAmounts, Long version) {
        this.order = order;
        this.orderArticles = orderArticles;
        this.referenceAmounts = referenceAmounts;
        this.version = version;
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "OrderSummary{" +
                "order=" + order +
                ", orderArticles=" + orderArticles +
                ", referenceAmounts=" + referenceAmounts +
                ", version=" + version +
                '}';
    }

}
