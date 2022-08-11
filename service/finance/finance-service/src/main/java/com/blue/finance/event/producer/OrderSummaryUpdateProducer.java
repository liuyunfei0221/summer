package com.blue.finance.event.producer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.finance.config.blue.BlueProducerConfig;
import com.blue.finance.repository.entity.OrderSummary;
import com.blue.pulsar.common.BluePulsarProducer;
import org.apache.pulsar.client.api.MessageId;
import reactor.util.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.ORDER_SUMMARY_UPDATE;
import static com.blue.pulsar.api.generator.BluePulsarProducerGenerator.generateProducer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static reactor.util.Loggers.getLogger;

/**
 * order summary update producer
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public final class OrderSummaryUpdateProducer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(OrderSummaryUpdateProducer.class);

    private final ExecutorService executorService;

    private final BluePulsarProducer<OrderSummary> orderSummaryUpdateProducer;

    public OrderSummaryUpdateProducer(ExecutorService executorService, BlueProducerConfig blueProducerConfig) {
        this.executorService = executorService;
        this.orderSummaryUpdateProducer = generateProducer(blueProducerConfig.getByKey(ORDER_SUMMARY_UPDATE.name), OrderSummary.class);
    }

    @Override
    public int startPrecedence() {
        return MIN_VALUE;
    }

    @Override
    public int stopPrecedence() {
        return MAX_VALUE;
    }

    @Override
    public void start() {
        LOGGER.warn("orderSummaryUpdateProducer start...");
    }

    @Override
    public void stop() {
        this.orderSummaryUpdateProducer.flush();
        this.orderSummaryUpdateProducer.close();
        LOGGER.warn("orderSummaryUpdateProducer shutdown...");
    }

    /**
     * send message
     *
     * @param orderSummary
     */
    public void send(OrderSummary orderSummary) {
        CompletableFuture<MessageId> future = orderSummaryUpdateProducer.sendAsync(orderSummary);
        LOGGER.info("orderSummaryUpdateProducer send, orderSummary = {}", orderSummary);
        Consumer<MessageId> c = messageId ->
                LOGGER.info("orderSummaryUpdateProducer send success, orderSummary = {}, messageId = {}", orderSummary, messageId);
        future.thenAcceptAsync(c, executorService);
    }

}
