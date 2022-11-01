package com.blue.finance.service.inter;

import com.blue.finance.repository.entity.Order;
import com.blue.finance.repository.entity.OrderArticle;
import com.blue.finance.repository.entity.ReferenceAmount;

import java.util.List;

/**
 * order service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "UnusedReturnValue"})
public interface OrderProcessService {

    /**
     * insert order
     *
     * @param order
     * @param orderArticles
     * @param referenceAmounts
     * @return
     */
    void insertOrder(Order order, List<OrderArticle> orderArticles, List<ReferenceAmount> referenceAmounts);

    /**
     * update order
     *
     * @param order
     * @param orderArticles
     * @param referenceAmounts
     * @return
     */
    void updateOrder(Order order, List<OrderArticle> orderArticles, List<ReferenceAmount> referenceAmounts);

    /**
     * update order
     *
     * @param order
     * @return
     */
    void updateOrder(Order order);

    /**
     * update order articles
     *
     * @param orderArticles
     * @return
     */
    void updateOrderArticles(List<OrderArticle> orderArticles);

    /**
     * update reference amounts
     *
     * @param referenceAmounts
     * @return
     */
    void updateReferenceAmounts(List<ReferenceAmount> referenceAmounts);

}