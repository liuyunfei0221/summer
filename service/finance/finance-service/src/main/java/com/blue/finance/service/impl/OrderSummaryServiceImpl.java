package com.blue.finance.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import com.blue.basic.model.common.Pit;
import com.blue.basic.model.common.ScrollModelRequest;
import com.blue.basic.model.common.ScrollModelResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.finance.event.producer.OrderSummaryInsertProducer;
import com.blue.finance.event.producer.OrderSummaryUpdateProducer;
import com.blue.finance.model.OrderCondition;
import com.blue.finance.repository.entity.OrderSummary;
import com.blue.finance.service.inter.OrderSummaryService;
import com.blue.identity.component.BlueIdentityProcessor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Optional;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;
import static reactor.util.Loggers.getLogger;

/**
 * order summary service impl
 *
 * @author liuyunfei
 */
@Service
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "SpringJavaInjectionPointsAutowiringInspection"})
public class OrderSummaryServiceImpl implements OrderSummaryService {

    private static final Logger LOGGER = getLogger(OrderSummaryServiceImpl.class);

//    private final BlueIdentityProcessor blueIdentityProcessor;
//
//    private final AsyncConnection asyncConnection;
//
//    private final ElasticsearchAsyncClient elasticsearchAsyncClient;
//
//    private final OrderSummaryInsertProducer orderSummaryInsertProducer;
//
//    private final OrderSummaryUpdateProducer orderSummaryUpdateProducer;
//
//    public OrderSummaryServiceImpl(BlueIdentityProcessor blueIdentityProcessor, AsyncConnection asyncConnection, ElasticsearchAsyncClient elasticsearchAsyncClient,
//                                   OrderSummaryInsertProducer orderSummaryInsertProducer, OrderSummaryUpdateProducer orderSummaryUpdateProducer) {
//        this.blueIdentityProcessor = blueIdentityProcessor;
//        this.asyncConnection = asyncConnection;
//        this.elasticsearchAsyncClient = elasticsearchAsyncClient;
//        this.orderSummaryInsertProducer = orderSummaryInsertProducer;
//        this.orderSummaryUpdateProducer = orderSummaryUpdateProducer;
//    }

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final ElasticsearchAsyncClient elasticsearchAsyncClient;

    private final OrderSummaryInsertProducer orderSummaryInsertProducer;

    private final OrderSummaryUpdateProducer orderSummaryUpdateProducer;

    public OrderSummaryServiceImpl(BlueIdentityProcessor blueIdentityProcessor, ElasticsearchAsyncClient elasticsearchAsyncClient,
                                   OrderSummaryInsertProducer orderSummaryInsertProducer, OrderSummaryUpdateProducer orderSummaryUpdateProducer) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.elasticsearchAsyncClient = elasticsearchAsyncClient;
        this.orderSummaryInsertProducer = orderSummaryInsertProducer;
        this.orderSummaryUpdateProducer = orderSummaryUpdateProducer;
    }

    /**
     * insert order summary
     *
     * @param orderSummary
     * @return
     */
    @Override
    public OrderSummary insertOrderSummary(OrderSummary orderSummary) {
        LOGGER.info("OrderSummary insertOrderSummary(OrderSummary orderSummary), orderSummary = {}", orderSummary);
        if (isNull(orderSummary))
            throw new BlueException(INVALID_PARAM);


        return orderSummary;
    }

    /**
     * insert order summary async
     *
     * @param orderSummary
     * @return
     */
    @Override
    public OrderSummary insertOrderSummaryAsync(OrderSummary orderSummary) {

        return null;
    }

    /**
     * update order summary
     *
     * @param orderSummary
     * @return
     */
    @Override
    public OrderSummary updateOrderSummary(OrderSummary orderSummary) {
        LOGGER.info("OrderSummary updateOrderSummary(OrderSummary orderSummary), orderSummary = {}", orderSummary);


        return null;
    }

    /**
     * update order summary async
     *
     * @param orderSummary
     * @return
     */
    @Override
    public OrderSummary updateOrderSummaryAsync(OrderSummary orderSummary) {
        LOGGER.info("OrderSummary updateOrderSummaryAsync(OrderSummary orderSummary), orderSummary = {}", orderSummary);


//        orderSummaryProducer.send(orderSummary);


        return orderSummary;
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
    public Mono<ScrollModelResponse<OrderSummary, Pit>> selectOrderSummaryByScrollAndMemberId(ScrollModelRequest<OrderCondition, Pit> pageModelRequest, Long memberId) {
        return null;
    }

}