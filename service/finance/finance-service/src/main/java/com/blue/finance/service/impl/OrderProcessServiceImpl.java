package com.blue.finance.service.impl;

import com.blue.finance.api.model.OrderInfo;
import com.blue.finance.repository.entity.Order;
import com.blue.finance.repository.entity.OrderArticle;
import com.blue.finance.repository.entity.ReferenceAmount;
import com.blue.finance.service.inter.*;
import org.springframework.stereotype.Service;
import reactor.util.Logger;

import java.util.List;

import static reactor.util.Loggers.getLogger;

/**
 * order processor service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class OrderProcessServiceImpl implements OrderProcessService {

    private static final Logger LOGGER = getLogger(OrderProcessServiceImpl.class);

    private final OrderService orderService;

    private final OrderArticleService orderArticleService;

    private final ReferenceAmountService referenceAmountService;

    private final OrderSummaryService orderSummaryService;

    public OrderProcessServiceImpl(OrderService orderService, OrderArticleService orderArticleService, ReferenceAmountService referenceAmountService, OrderSummaryService orderSummaryService) {
        this.orderService = orderService;
        this.orderArticleService = orderArticleService;
        this.referenceAmountService = referenceAmountService;
        this.orderSummaryService = orderSummaryService;
    }

    /**
     * insert order
     *
     * @param order
     * @param orderArticles
     * @param referenceAmounts
     * @return
     */
    public OrderInfo insertOrder(Order order, List<OrderArticle> orderArticles, List<ReferenceAmount> referenceAmounts) {
        return null;
    }

    /**
     * update order
     *
     * @param order
     * @param orderArticles
     * @param referenceAmounts
     * @return
     */
    public OrderInfo updateOrder(Order order, List<OrderArticle> orderArticles, List<ReferenceAmount> referenceAmounts) {
        return null;
    }

    /**
     * delete order
     *
     * @param id
     * @return
     */
    public Boolean deleteOrder(Long id) {
        return null;
    }

}
