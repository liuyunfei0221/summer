package com.blue.finance.repository.entity;

import java.io.Serializable;
import java.util.List;

/**
 * order summary in hbase
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public class OrderSummary implements Serializable {

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
