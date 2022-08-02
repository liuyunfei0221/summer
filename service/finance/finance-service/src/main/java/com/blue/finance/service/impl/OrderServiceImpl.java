package com.blue.finance.service.impl;

import com.blue.basic.model.exps.BlueException;
import com.blue.finance.repository.entity.Order;
import com.blue.finance.repository.mapper.OrderMapper;
import com.blue.finance.service.inter.OrderService;
import com.blue.identity.component.BlueIdentityProcessor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Optional;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.justOrEmpty;
import static reactor.util.Loggers.getLogger;

/**
 * order service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "SpringJavaInjectionPointsAutowiringInspection"})
@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger LOGGER = getLogger(OrderServiceImpl.class);

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final OrderMapper orderMapper;

    public OrderServiceImpl(BlueIdentityProcessor blueIdentityProcessor, OrderMapper orderMapper) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.orderMapper = orderMapper;
    }

    /**
     * insert order
     *
     * @param order
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public Order insertOrder(Order order) {
        LOGGER.info("Order insertOrder(Order order), order = {}", order);
        if (isNull(order))
            throw new BlueException(EMPTY_PARAM);

        if (isInvalidIdentity(order.getId()))
            order.setId(blueIdentityProcessor.generate(Order.class));

        orderMapper.insert(order);

        return order;
    }

    /**
     * update a exist order
     *
     * @param order
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public Boolean updateOrder(Order order) {
        LOGGER.info("Order updateOrder(Order order), order = {}", order);
        if (isNull(order))
            throw new BlueException(EMPTY_PARAM);

        if (isInvalidIdentity(order.getId()))
            throw new BlueException(INVALID_IDENTITY);

        int updated = orderMapper.updateByPrimaryKeySelective(order);
        LOGGER.info("order = {}, updated = {}", order, updated);

        return updated > 0;
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
        LOGGER.info("Boolean deleteOrder(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        int deleted = orderMapper.deleteByPrimaryKey(id);
        LOGGER.info("deleted = {}", deleted);

        return deleted > 0;
    }

    /**
     * get order by id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Order> getOrder(Long id) {
        LOGGER.info("Optional<Order> getOrder(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return ofNullable(orderMapper.selectByPrimaryKey(id));
    }

    /**
     * get order mono by role id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Order> getOrderMono(Long id) {
        LOGGER.info("Mono<Order> getOrderMono(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return justOrEmpty(orderMapper.selectByPrimaryKey(id));
    }

    /**
     * select orders by ids
     *
     * @param ids
     * @return
     */
    @Override
    public List<Order> selectOrderByIds(List<Long> ids) {
        LOGGER.info("List<Order> selectOrderByIds(List<Long> ids), ids = {}", ids);
        if (isEmpty(ids))
            return emptyList();

        return orderMapper.selectByIds(ids);
    }

    /**
     * select orders mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<Order>> selectOrderMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<List<Order>> selectOrderMonoByIds(List<Long> ids), ids = {}", ids);

        return just(selectOrderByIds(ids));
    }

}