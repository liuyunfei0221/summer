package com.blue.finance.event.producer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.finance.config.blue.BlueProducerConfig;
import com.blue.finance.repository.entity.OrderSummary;
import com.blue.pulsar.component.BluePulsarProducer;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.PulsarClient;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.ORDER_SUMMARY_UPDATE;
import static com.blue.pulsar.api.generator.BluePulsarProducerGenerator.generateProducer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * order summary update producer
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public final class OrderSummaryUpdateProducer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(OrderSummaryUpdateProducer.class);

    private final ExecutorService executorService;

    private final BluePulsarProducer<OrderSummary> pulsarProducer;

    public OrderSummaryUpdateProducer(PulsarClient pulsarClient, ExecutorService executorService, BlueProducerConfig blueProducerConfig) {
        this.executorService = executorService;
        this.pulsarProducer = generateProducer(pulsarClient, blueProducerConfig.getByKey(ORDER_SUMMARY_UPDATE.name), OrderSummary.class);
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
        LOGGER.warn("pulsarProducer start...");
    }

    @Override
    public void stop() {
        this.pulsarProducer.flush();
        this.pulsarProducer.close();
        LOGGER.warn("pulsarProducer shutdown...");
    }

    /**
     * send message
     *
     * @param orderSummary
     */
    public void send(OrderSummary orderSummary) {
        CompletableFuture<MessageId> future = pulsarProducer.sendAsync(orderSummary);
        LOGGER.info("pulsarProducer send, orderSummary = {}", orderSummary);
        Consumer<MessageId> c = messageId ->
                LOGGER.info("pulsarProducer send success, orderSummary = {}, messageId = {}", orderSummary, messageId);
        future.thenAcceptAsync(c, executorService);
    }

}
