package com.blue.finance.service.impl;

import com.blue.basic.model.exps.BlueException;
import com.blue.finance.api.model.OrderInfo;
import com.blue.finance.repository.entity.Order;
import com.blue.finance.repository.entity.OrderArticle;
import com.blue.finance.repository.entity.OrderSummary;
import com.blue.finance.repository.entity.ReferenceAmount;
import com.blue.finance.service.inter.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.Logger;

import java.util.List;

import static com.blue.basic.common.base.BlueChecker.isEmpty;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;
import static com.blue.finance.converter.FinanceModelConverters.ORDER_2_ORDER_INFO_CONVERTER;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
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
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public OrderInfo insertOrder(Order order, List<OrderArticle> orderArticles, List<ReferenceAmount> referenceAmounts) {
        LOGGER.info("OrderInfo insertOrder(Order order, List<OrderArticle> orderArticles, List<ReferenceAmount> referenceAmounts), order = {}, orderArticles = {}, referenceAmounts = {}", order, orderArticles, referenceAmounts);
        if (isNull(order) || isEmpty(orderArticles) || isEmpty(referenceAmounts))
            throw new BlueException(INVALID_PARAM);

        Order insertedOrder = orderService.insertOrder(order);
        LOGGER.info("insertedOrder = {}", insertedOrder);

        List<OrderArticle> insertedOrderArticles = orderArticleService.insertOrderArticles(orderArticles);
        LOGGER.info("insertedOrderArticles = {}", insertedOrderArticles);

        List<ReferenceAmount> insertedReferenceAmounts = referenceAmountService.insertReferenceAmounts(referenceAmounts);
        LOGGER.info("insertedReferenceAmounts = {}", insertedReferenceAmounts);

        OrderSummary orderSummary = orderSummaryService.insertOrderSummary(new OrderSummary(order.getId(), order, orderArticles, referenceAmounts, order.getVersion()));
        LOGGER.info("orderSummary = {}", orderSummary);

        return ORDER_2_ORDER_INFO_CONVERTER.apply(insertedOrder);
    }

    /**
     * update order
     *
     * @param order
     * @param orderArticles
     * @param referenceAmounts
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public OrderInfo updateOrder(Order order, List<OrderArticle> orderArticles, List<ReferenceAmount> referenceAmounts) {


        return null;
    }

    /**
     * delete order
     *
     * @param id
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public Boolean deleteOrder(Long id) {
        return null;
    }

}
