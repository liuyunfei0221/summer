package com.blue.finance.service.impl;

import com.blue.basic.model.exps.BlueException;
import com.blue.finance.model.db.OrderArticleUpdateModel;
import com.blue.finance.model.db.OrderUpdateModel;
import com.blue.finance.model.db.ReferenceAmountUpdateModel;
import com.blue.finance.repository.entity.Order;
import com.blue.finance.repository.entity.OrderArticle;
import com.blue.finance.repository.entity.OrderSummary;
import com.blue.finance.repository.entity.ReferenceAmount;
import com.blue.finance.service.inter.*;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.constant.common.ResponseElement.*;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

/**
 * order processor service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
@Service
public class OrderProcessServiceImpl implements OrderProcessService {

    private static final Logger LOGGER = getLogger(OrderProcessServiceImpl.class);

    private OrderService orderService;

    private OrderArticleService orderArticleService;

    private ReferenceAmountService referenceAmountService;

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
    public void insertOrder(Order order, List<OrderArticle> orderArticles, List<ReferenceAmount> referenceAmounts) {
        LOGGER.info("order = {}, orderArticles = {}, referenceAmounts = {}",
                order, orderArticles, referenceAmounts);
        if (isNull(order) || isEmpty(orderArticles) || isEmpty(referenceAmounts))
            throw new BlueException(INVALID_PARAM);

        Order insertedOrder = orderService.insertOrder(order);
        LOGGER.info("insertedOrder = {}", insertedOrder);

        List<OrderArticle> insertedOrderArticles = orderArticleService.insertOrderArticles(orderArticles);
        LOGGER.info("insertedOrderArticles = {}", insertedOrderArticles);

        List<ReferenceAmount> insertedReferenceAmounts = referenceAmountService.insertReferenceAmounts(referenceAmounts);
        LOGGER.info("insertedReferenceAmounts = {}", insertedReferenceAmounts);

        OrderSummary orderSummary = orderSummaryService.insertOrderSummaryAsync(new OrderSummary(order.getId(), order, orderArticles, referenceAmounts, order.getVersion()));
        LOGGER.info("orderSummary = {}", orderSummary);
    }

    private final BiFunction<Order, Long, OrderUpdateModel> ORDER_UPDATE_MODEL_BY_ORDER_GEN = (order, now) -> {
        Long orderId = order.getId();
        if (isInvalidIdentity(orderId))
            throw new BlueException(INVALID_IDENTITY);

        Optional<Order> orderOpt = orderService.getOrder(orderId);
        if (orderOpt.isEmpty())
            throw new BlueException(DATA_NOT_EXIST);

        Order originalOrder = orderOpt.get();

        OrderUpdateModel orderUpdateModel = new OrderUpdateModel();

        orderUpdateModel.setId(orderId);
        ofNullable(order.getPaymentTime()).ifPresent(orderUpdateModel::setPaymentTime);
        ofNullable(order.getExtra()).ifPresent(orderUpdateModel::setExtra);
        ofNullable(order.getPaymentExtra()).ifPresent(orderUpdateModel::setPaymentExtra);

        Integer originalOrderStatus = originalOrder.getStatus();
        ofNullable(order.getStatus()).filter(status -> !originalOrderStatus.equals(status))
                .ifPresent(status -> {
                    orderUpdateModel.setOriginalStatus(originalOrderStatus);
                    orderUpdateModel.setDestStatus(status);
                });

        Integer originalVersion = originalOrder.getVersion();
        Integer destVersion = originalVersion + 1;

        orderUpdateModel.setOriginalVersion(originalVersion);
        orderUpdateModel.setDestVersion(destVersion);
        orderUpdateModel.setUpdateTime(now);

        return orderUpdateModel;
    };

    private final BiFunction<Long, Long, OrderUpdateModel> ORDER_UPDATE_MODEL_BY_ORDER_ID_GEN = (orderId, now) -> {
        if (isInvalidIdentity(orderId))
            throw new BlueException(INVALID_IDENTITY);

        Optional<Order> orderOpt = orderService.getOrder(orderId);
        if (orderOpt.isEmpty())
            throw new BlueException(DATA_NOT_EXIST);

        Order originalOrder = orderOpt.get();

        OrderUpdateModel orderUpdateModel = new OrderUpdateModel();

        orderUpdateModel.setId(orderId);

        Integer originalVersion = originalOrder.getVersion();
        Integer destVersion = originalVersion + 1;

        orderUpdateModel.setOriginalVersion(originalVersion);
        orderUpdateModel.setDestVersion(destVersion);
        orderUpdateModel.setUpdateTime(now);

        return orderUpdateModel;
    };

    private final BiFunction<List<OrderArticle>, Long, List<OrderArticleUpdateModel>> ORDER_ARTICLE_UPDATE_MODELS_GEN = (orderArticles, now) -> {
        if (isEmpty(orderArticles))
            return emptyList();

        Map<Long, OrderArticle> idAndOrderArticleMapping = orderArticleService.selectOrderArticleByIds(orderArticles.stream().map(OrderArticle::getId).collect(toList()))
                .stream().collect(toMap(OrderArticle::getId, identity(), (a, b) -> a));
        if (idAndOrderArticleMapping.entrySet().size() != orderArticles.size())
            throw new BlueException(INVALID_PARAM);

        return orderArticles.stream().map(orderArticle -> {
            Long orderArticleId = orderArticle.getId();

            OrderArticleUpdateModel orderArticleUpdateModel = new OrderArticleUpdateModel();

            orderArticleUpdateModel.setId(orderArticleId);
            ofNullable(orderArticle.getExtra()).ifPresent(orderArticleUpdateModel::setExtra);

            Integer originalOrderArticleStatus = idAndOrderArticleMapping.get(orderArticleId).getStatus();
            ofNullable(orderArticle.getStatus()).filter(status -> !originalOrderArticleStatus.equals(status))
                    .ifPresent(status -> {
                        orderArticleUpdateModel.setOriginalStatus(originalOrderArticleStatus);
                        orderArticleUpdateModel.setDestStatus(status);
                    });

            orderArticleUpdateModel.setUpdateTime(now);

            return orderArticleUpdateModel;
        }).collect(toList());
    };

    private final BiFunction<List<ReferenceAmount>, Long, List<ReferenceAmountUpdateModel>> REFERENCE_AMOUNT_UPDATE_MODELS_GEN = (referenceAmounts, now) -> {
        if (isEmpty(referenceAmounts))
            return emptyList();

        Map<Long, ReferenceAmount> idAndReferenceAmountMapping = referenceAmountService.selectReferenceAmountByIds(referenceAmounts.stream().map(ReferenceAmount::getId).collect(toList()))
                .stream().collect(toMap(ReferenceAmount::getId, identity(), (a, b) -> a));
        if (idAndReferenceAmountMapping.entrySet().size() != referenceAmounts.size())
            throw new BlueException(INVALID_PARAM);

        return referenceAmounts.stream().map(referenceAmount -> {
            Long referenceAmountId = referenceAmount.getId();

            ReferenceAmountUpdateModel referenceAmountUpdateModel = new ReferenceAmountUpdateModel();

            referenceAmountUpdateModel.setId(referenceAmountId);
            ofNullable(referenceAmount.getExtra()).ifPresent(referenceAmountUpdateModel::setExtra);

            Integer originalReferenceAmountStatus = idAndReferenceAmountMapping.get(referenceAmountId).getStatus();
            ofNullable(referenceAmount.getStatus()).filter(status -> !originalReferenceAmountStatus.equals(status))
                    .ifPresent(status -> {
                        referenceAmountUpdateModel.setOriginalStatus(originalReferenceAmountStatus);
                        referenceAmountUpdateModel.setDestStatus(status);
                    });

            referenceAmountUpdateModel.setUpdateTime(now);

            return referenceAmountUpdateModel;
        }).collect(toList());
    };

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
    public void updateOrder(Order order, List<OrderArticle> orderArticles, List<ReferenceAmount> referenceAmounts) {
        LOGGER.info("order = {}, orderArticles = {}, referenceAmounts = {}",
                order, orderArticles, referenceAmounts);
        if (isNull(order) || isEmpty(orderArticles) || isEmpty(referenceAmounts))
            throw new BlueException(INVALID_PARAM);

        Long orderId = order.getId();
        if (isInvalidIdentity(orderId))
            throw new BlueException(INVALID_IDENTITY);
        if (!orderArticles.stream().map(OrderArticle::getOrderId).allMatch(orderId::equals))
            throw new BlueException(INVALID_PARAM);
        if (!referenceAmounts.stream().map(ReferenceAmount::getOrderId).allMatch(orderId::equals))
            throw new BlueException(INVALID_PARAM);

        Optional<Order> orderOpt = orderService.getOrder(orderId);
        if (orderOpt.isEmpty())
            throw new BlueException(DATA_NOT_EXIST);

        Long now = TIME_STAMP_GETTER.get();

        OrderUpdateModel orderUpdateModel = ORDER_UPDATE_MODEL_BY_ORDER_GEN.apply(order, now);
        Integer destVersion = orderUpdateModel.getDestVersion();
        List<OrderArticleUpdateModel> orderArticleUpdateModels = ORDER_ARTICLE_UPDATE_MODELS_GEN.apply(orderArticles, now);
        List<ReferenceAmountUpdateModel> referenceAmountUpdateModels = REFERENCE_AMOUNT_UPDATE_MODELS_GEN.apply(referenceAmounts, now);

        orderService.updateTargetColumnByPrimaryKeySelectiveWithStamps(orderUpdateModel);
        orderArticleUpdateModels.forEach(orderArticleService::updateTargetColumnByPrimaryKeySelectiveWithStatusStamp);
        referenceAmountUpdateModels.forEach(referenceAmountService::updateTargetColumnByPrimaryKeySelectiveWithStatusStamp);

        order.setVersion(destVersion);

        orderSummaryService.updateOrderSummaryAsync(new OrderSummary(orderId, order, orderArticles, referenceAmounts, destVersion));
    }

    /**
     * update order
     *
     * @param order
     * @return
     */
    @Override
    public void updateOrder(Order order) {
        LOGGER.info("order = {}", order);
        if (isNull(order))
            throw new BlueException(INVALID_PARAM);

        Long orderId = order.getId();
        if (isInvalidIdentity(orderId))
            throw new BlueException(INVALID_IDENTITY);

        OrderUpdateModel orderUpdateModel = ORDER_UPDATE_MODEL_BY_ORDER_GEN.apply(order, TIME_STAMP_GETTER.get());
        Integer destVersion = orderUpdateModel.getDestVersion();

        order.setVersion(destVersion);

        orderSummaryService.updateOrderSummaryAsync(new OrderSummary(orderId, order, null, null, destVersion));
    }

    /**
     * update order articles
     *
     * @param orderArticles
     * @return
     */
    @Override
    public void updateOrderArticles(List<OrderArticle> orderArticles) {
        LOGGER.info("orderArticles = {}", orderArticles);
        if (isEmpty(orderArticles))
            throw new BlueException(INVALID_PARAM);

        Long orderId = orderArticles.get(0).getOrderId();
        if (isInvalidIdentity(orderId))
            throw new BlueException(INVALID_IDENTITY);
        if (!orderArticles.stream().map(OrderArticle::getOrderId).allMatch(orderId::equals))
            throw new BlueException(INVALID_PARAM);

        Optional<Order> orderOpt = orderService.getOrder(orderId);
        if (orderOpt.isEmpty())
            throw new BlueException(DATA_NOT_EXIST);

        Order originalOrder = orderOpt.get();

        Long now = TIME_STAMP_GETTER.get();

        OrderUpdateModel orderUpdateModel = ORDER_UPDATE_MODEL_BY_ORDER_ID_GEN.apply(orderId, now);
        Integer destVersion = orderUpdateModel.getDestVersion();
        List<OrderArticleUpdateModel> orderArticleUpdateModels = ORDER_ARTICLE_UPDATE_MODELS_GEN.apply(orderArticles, now);

        orderService.updateTargetColumnByPrimaryKeySelectiveWithStamps(orderUpdateModel);
        orderArticleUpdateModels.forEach(orderArticleService::updateTargetColumnByPrimaryKeySelectiveWithStatusStamp);

        originalOrder.setVersion(destVersion);

        orderSummaryService.updateOrderSummaryAsync(new OrderSummary(orderId, originalOrder, orderArticles, null, destVersion));
    }

    /**
     * update reference amounts
     *
     * @param referenceAmounts
     * @return
     */
    @Override
    public void updateReferenceAmounts(List<ReferenceAmount> referenceAmounts) {
        LOGGER.info("referenceAmounts = {}", referenceAmounts);
        if (isEmpty(referenceAmounts))
            throw new BlueException(INVALID_PARAM);

        Long orderId = referenceAmounts.get(0).getOrderId();
        if (isInvalidIdentity(orderId))
            throw new BlueException(INVALID_IDENTITY);
        if (!referenceAmounts.stream().map(ReferenceAmount::getOrderId).allMatch(orderId::equals))
            throw new BlueException(INVALID_PARAM);

        Optional<Order> orderOpt = orderService.getOrder(orderId);
        if (orderOpt.isEmpty())
            throw new BlueException(DATA_NOT_EXIST);

        Order originalOrder = orderOpt.get();

        Long now = TIME_STAMP_GETTER.get();

        OrderUpdateModel orderUpdateModel = ORDER_UPDATE_MODEL_BY_ORDER_ID_GEN.apply(orderId, now);
        Integer destVersion = orderUpdateModel.getDestVersion();

        List<ReferenceAmountUpdateModel> referenceAmountUpdateModels = REFERENCE_AMOUNT_UPDATE_MODELS_GEN.apply(referenceAmounts, now);

        orderService.updateTargetColumnByPrimaryKeySelectiveWithStamps(orderUpdateModel);
        referenceAmountUpdateModels.forEach(referenceAmountService::updateTargetColumnByPrimaryKeySelectiveWithStatusStamp);

        originalOrder.setVersion(destVersion);

        orderSummaryService.updateOrderSummaryAsync(new OrderSummary(orderId, originalOrder, null, referenceAmounts, destVersion));
    }

}
