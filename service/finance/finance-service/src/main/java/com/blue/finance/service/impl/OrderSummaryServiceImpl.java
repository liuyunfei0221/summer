package com.blue.finance.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.blue.basic.model.common.Pit;
import com.blue.basic.model.common.ScrollModelRequest;
import com.blue.basic.model.common.ScrollModelResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.finance.repository.entity.Order;
import com.blue.finance.repository.entity.OrderSummary;
import com.blue.finance.service.inter.OrderSummaryService;
import com.blue.identity.component.BlueIdentityProcessor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.io.IOException;
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
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
public class OrderSummaryServiceImpl implements OrderSummaryService {

    private static final Logger LOGGER = getLogger(OrderSummaryServiceImpl.class);

//    private final BlueIdentityProcessor blueIdentityProcessor;
//
//    private final AsyncConnection asyncConnection;
//
//    private final ElasticsearchClient elasticsearchClient;
//
//    private final OrderSummaryProducer orderSummaryProducer;
//
//    public OrderSummaryServiceImpl(BlueIdentityProcessor blueIdentityProcessor, AsyncConnection asyncConnection, ElasticsearchClient elasticsearchClient, OrderSummaryProducer orderSummaryProducer) {
//        this.blueIdentityProcessor = blueIdentityProcessor;
//        this.asyncConnection = asyncConnection;
//        this.elasticsearchClient = elasticsearchClient;
//        this.orderSummaryProducer = orderSummaryProducer;
//    }

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final ElasticsearchClient elasticsearchClient;

    public OrderSummaryServiceImpl(BlueIdentityProcessor blueIdentityProcessor, ElasticsearchClient elasticsearchClient) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.elasticsearchClient = elasticsearchClient;
    }

    @PostConstruct
    private void init() {

        try {
//            CreateIndexResponse response = elasticsearchClient.indices().create(c -> c.index("order-summary"));
//            System.err.println(response);

            OrderSummary orderSummary = new OrderSummary();
            orderSummary.setId(blueIdentityProcessor.generate(OrderSummary.class));
            orderSummary.setOrder(new Order());

            IndexResponse indexResponse = elasticsearchClient.index(i ->
                    i.index("order-summary")
                            .id(String.valueOf(blueIdentityProcessor.generate(OrderSummary.class)))
                            .document(orderSummary));
            System.err.println(indexResponse);
        } catch (IOException e) {
            LOGGER.error("init() failed, e = {0}", e);
        }
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