package com.blue.finance.service.inter;

import com.blue.finance.api.model.OrderInfo;
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
    OrderInfo insertOrder(Order order, List<OrderArticle> orderArticles, List<ReferenceAmount> referenceAmounts);

    /**
     * update order
     *
     * @param order
     * @param orderArticles
     * @param referenceAmounts
     * @return
     */
    OrderInfo updateOrder(Order order, List<OrderArticle> orderArticles, List<ReferenceAmount> referenceAmounts);

    /**
     * delete order
     *
     * @param id
     * @return
     */
    Boolean deleteOrder(Long id);

}