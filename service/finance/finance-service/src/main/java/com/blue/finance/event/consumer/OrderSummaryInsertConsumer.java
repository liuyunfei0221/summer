package com.blue.finance.event.consumer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.exps.BlueException;
import com.blue.finance.config.blue.BlueConsumerConfig;
import com.blue.finance.repository.entity.OrderSummary;
import com.blue.finance.service.inter.OrderSummaryService;
import com.blue.pulsar.component.BluePulsarListener;
import org.apache.pulsar.client.api.PulsarClient;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.ORDER_SUMMARY_INSERT;
import static com.blue.basic.constant.common.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.pulsar.api.generator.BluePulsarListenerGenerator.generateListener;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;


/**
 * order summary insert consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public final class OrderSummaryInsertConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(OrderSummaryInsertConsumer.class);

    private final PulsarClient pulsarClient;

    private final BlueConsumerConfig blueConsumerConfig;

    private final OrderSummaryService orderSummaryService;

    private BluePulsarListener<OrderSummary> pulsarListener;

    public OrderSummaryInsertConsumer(PulsarClient pulsarClient, BlueConsumerConfig blueConsumerConfig, OrderSummaryService orderSummaryService) {
        this.pulsarClient = pulsarClient;
        this.blueConsumerConfig = blueConsumerConfig;
        this.orderSummaryService = orderSummaryService;
    }

    @PostConstruct
    private void init() {
        Consumer<OrderSummary> dataConsumer = orderSummary ->
                ofNullable(orderSummary)
                        .ifPresent(os -> just(os).map(orderSummaryService::insertOrderSummary)
                                .switchIfEmpty(defer(() -> error(() -> new BlueException(INTERNAL_SERVER_ERROR))))
                                .doOnError(throwable -> LOGGER.info("orderSummaryService.insertOrderSummary(os) failed, os = {}, throwable = {}", os, throwable))
                                .subscribe(b -> LOGGER.info("orderSummaryService.insertOrderSummary(os), b = {}, os = {}", b, os)));

        this.pulsarListener = generateListener(pulsarClient, blueConsumerConfig.getByKey(ORDER_SUMMARY_INSERT.name), dataConsumer);
    }

    @Override
    public int startPrecedence() {
        return MAX_VALUE;
    }

    @Override
    public int stopPrecedence() {
        return MIN_VALUE;
    }

    @Override
    public void start() {
        this.pulsarListener.run();
        LOGGER.warn("pulsarListener start...");
    }

    @Override
    public void stop() {
        this.pulsarListener.shutdown();
        LOGGER.warn("pulsarListener shutdown...");
    }

}
