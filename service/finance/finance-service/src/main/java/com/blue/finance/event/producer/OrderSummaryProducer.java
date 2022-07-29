package com.blue.finance.event.producer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.finance.config.blue.BlueProducerConfig;
import com.blue.finance.model.OrderSummary;
import com.blue.pulsar.common.BluePulsarProducer;
import org.apache.pulsar.client.api.MessageId;
import reactor.util.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.ORDER_SUMMARY;
import static com.blue.pulsar.api.generator.BluePulsarProducerGenerator.generateProducer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static reactor.util.Loggers.getLogger;

/**
 * order summary producer
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public final class OrderSummaryProducer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(OrderSummaryProducer.class);

    private final ExecutorService executorService;

    private final BluePulsarProducer<OrderSummary> orderSummaryProducer;

    public OrderSummaryProducer(ExecutorService executorService, BlueProducerConfig blueProducerConfig) {
        this.executorService = executorService;
        this.orderSummaryProducer = generateProducer(blueProducerConfig.getByKey(ORDER_SUMMARY.name), OrderSummary.class);
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
        LOGGER.warn("invalidLocalAccessProducer start...");
    }

    @Override
    public void stop() {
        this.orderSummaryProducer.flush();
        this.orderSummaryProducer.close();
        LOGGER.warn("orderSummaryProducer shutdown...");
    }

    /**
     * send message
     *
     * @param orderSummary
     */
    public void send(OrderSummary orderSummary) {
        CompletableFuture<MessageId> future = orderSummaryProducer.sendAsync(orderSummary);
        LOGGER.info("orderSummaryProducer send, orderSummary = {}", orderSummary);
        Consumer<MessageId> c = messageId ->
                LOGGER.info("orderSummaryProducer send success, orderSummary = {}, messageId = {}", orderSummary, messageId);
        future.thenAcceptAsync(c, executorService);
    }

}
