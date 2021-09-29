package com.blue.gateway.config.mq.producer;

import com.blue.base.component.lifecycle.inter.BlueLifecycle;
import com.blue.base.model.event.data.DataEvent;
import com.blue.gateway.config.blue.BlueProducerConfig;
import com.blue.pulsar.common.BluePulsarProducer;
import org.apache.pulsar.client.api.MessageId;
import reactor.util.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.base.constant.base.BlueTopic.REQUEST_EVENT;
import static com.blue.pulsar.api.generator.BluePulsarProducerGenerator.generateProducer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static reactor.util.Loggers.getLogger;


/**
 * data report producer
 *
 * @author DarkBlue
 */
@SuppressWarnings({"Duplicates", "JavaDoc"})
public final class RequestEventProducer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(RequestEventProducer.class);

    private final ExecutorService executorService;

    private final BluePulsarProducer<DataEvent> requestEventProducer;

    public RequestEventProducer(ExecutorService executorService, BlueProducerConfig blueProducerConfig) {
        this.executorService = executorService;
        this.requestEventProducer = generateProducer(blueProducerConfig.getByKey(REQUEST_EVENT.name), DataEvent.class);
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
        LOGGER.warn("requestEventProducer start...");
    }

    @Override
    public void stop() {
        this.requestEventProducer.shutdown();
        LOGGER.warn("requestEventProducer shutdown...");
    }

    /**
     * send event
     *
     * @param dataEvent
     */
    public void send(DataEvent dataEvent) {
        CompletableFuture<MessageId> future = requestEventProducer.sendAsync(dataEvent);

        LOGGER.info("requestEventProducer send, dataEvent = {}", dataEvent);
        Consumer<MessageId> c = messageId ->
                LOGGER.info("requestEventProducer send success, dataEvent = {}, messageId = {}", dataEvent, messageId);

        future.thenAcceptAsync(c, executorService);
    }

}
