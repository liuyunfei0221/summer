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

import static com.blue.basic.constant.common.BlueTopic.ORDER_SUMMARY_INSERT;
import static com.blue.pulsar.api.generator.BluePulsarProducerGenerator.generateProducer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static reactor.util.Loggers.getLogger;

/**
 * order summary insert producer
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public final class OrderSummaryInsertProducer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(OrderSummaryInsertProducer.class);

    private final ExecutorService executorService;

    private final BluePulsarProducer<OrderSummary> orderSummaryInsertProducer;

    public OrderSummaryInsertProducer(ExecutorService executorService, BlueProducerConfig blueProducerConfig) {
        this.executorService = executorService;
        this.orderSummaryInsertProducer = generateProducer(blueProducerConfig.getByKey(ORDER_SUMMARY_INSERT.name), OrderSummary.class);
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
        LOGGER.warn("orderSummaryInsertProducer start...");
    }

    @Override
    public void stop() {
        this.orderSummaryInsertProducer.flush();
        this.orderSummaryInsertProducer.close();
        LOGGER.warn("orderSummaryInsertProducer shutdown...");
    }

    /**
     * send message
     *
     * @param orderSummary
     */
    public void send(OrderSummary orderSummary) {
        CompletableFuture<MessageId> future = orderSummaryInsertProducer.sendAsync(orderSummary);
        LOGGER.info("orderSummaryInsertProducer send, orderSummary = {}", orderSummary);
        Consumer<MessageId> c = messageId ->
                LOGGER.info("orderSummaryInsertProducer send success, orderSummary = {}, messageId = {}", orderSummary, messageId);
        future.thenAcceptAsync(c, executorService);
    }

}
