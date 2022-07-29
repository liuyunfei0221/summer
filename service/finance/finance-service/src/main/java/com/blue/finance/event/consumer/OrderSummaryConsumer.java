package com.blue.finance.event.consumer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.finance.config.blue.BlueConsumerConfig;
import com.blue.finance.model.OrderSummary;
import com.blue.finance.service.inter.OrderSummaryService;
import com.blue.pulsar.api.generator.BluePulsarListenerGenerator;
import com.blue.pulsar.common.BluePulsarListener;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.ORDER_SUMMARY;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;


/**
 * order summary consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public final class OrderSummaryConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(OrderSummaryConsumer.class);

    private final BlueConsumerConfig blueConsumerConfig;

    private final OrderSummaryService orderSummaryService;

    private BluePulsarListener<OrderSummary> orderSummaryConsumer;

    public OrderSummaryConsumer(BlueConsumerConfig blueConsumerConfig, OrderSummaryService orderSummaryService) {
        this.blueConsumerConfig = blueConsumerConfig;
        this.orderSummaryService = orderSummaryService;
    }

    @PostConstruct
    private void init() {
        Consumer<OrderSummary> orderSummaryDataConsumer = orderSummary ->
                ofNullable(orderSummary)
                        .ifPresent(os -> orderSummaryService.insertOrderSummary(os)
                                .doOnError(throwable -> LOGGER.info("orderSummaryService.insertOrderSummary(os) failed, os = {}, throwable = {}", os, throwable))
                                .subscribe(b -> LOGGER.info("orderSummaryService.insertOrderSummary(os), b = {}, os = {}", b, os)));

        this.orderSummaryConsumer = BluePulsarListenerGenerator.generateListener(blueConsumerConfig.getByKey(ORDER_SUMMARY.name), orderSummaryDataConsumer);
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
        this.orderSummaryConsumer.run();
        LOGGER.warn("orderSummaryConsumer start...");
    }

    @Override
    public void stop() {
        this.orderSummaryConsumer.shutdown();
        LOGGER.warn("orderSummaryConsumer shutdown...");
    }

}
