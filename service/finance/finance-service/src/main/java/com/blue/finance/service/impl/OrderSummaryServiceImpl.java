package com.blue.finance.service.impl;

import com.blue.basic.model.common.Pit;
import com.blue.basic.model.common.ScrollModelRequest;
import com.blue.basic.model.common.ScrollModelResponse;
import com.blue.finance.repository.entity.OrderSummary;
import com.blue.finance.service.inter.OrderSummaryService;
import com.blue.identity.component.BlueIdentityProcessor;
import org.apache.hadoop.hbase.client.AsyncConnection;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Optional;

import static reactor.util.Loggers.getLogger;

/**
 * order summary service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "UnusedReturnValue"})
public class OrderSummaryServiceImpl implements OrderSummaryService {

    private static final Logger LOGGER = getLogger(OrderSummaryServiceImpl.class);

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final AsyncConnection asyncConnection;

    public OrderSummaryServiceImpl(BlueIdentityProcessor blueIdentityProcessor, AsyncConnection asyncConnection) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.asyncConnection = asyncConnection;
    }

    /**
     * insert order summary async
     *
     * @param orderSummary
     * @return
     */
    @Override
    public Mono<OrderSummary> insertOrderSummaryAsync(OrderSummary orderSummary) {
        return null;
    }

    /**
     * update order summary async
     *
     * @param orderSummary
     * @return
     */
    @Override
    public Mono<OrderSummary> updateOrderSummaryAsync(OrderSummary orderSummary) {
        return null;
    }

    /**
     * insert order summary
     *
     * @param orderSummary
     * @return
     */
    @Override
    public Mono<OrderSummary> insertOrderSummary(OrderSummary orderSummary) {
        return null;
    }

    /**
     * update order summary
     *
     * @param orderSummary
     * @return
     */
    @Override
    public Mono<OrderSummary> updateOrderSummary(OrderSummary orderSummary) {
        return null;
    }

    /**
     * get order summary mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<OrderSummary> getOrderSummaryMono(Long id) {
        return null;
    }

    /**
     * get order summary flow by id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<OrderSummary> getOrderSummary(Long id) {
        return null;
    }

    /**
     * select order summary by page and memberId
     *
     * @param rows
     * @param pit
     * @param memberId
     * @return
     */
    @Override
    public Mono<List<OrderSummary>> selectOrderSummaryMonoByPitAndMemberId(Long rows, Pit pit, Long memberId) {
        return null;
    }

    /**
     * select order summary by scroll and member id
     *
     * @param pageModelRequest
     * @param memberId
     * @return
     */
    @Override
    public Mono<ScrollModelResponse<OrderSummary, Pit>> selectOrderSummaryByScrollAndMemberId(ScrollModelRequest<Pit> pageModelRequest, Long memberId) {
        return null;
    }

}