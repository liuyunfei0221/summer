package com.blue.message.event.producer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.event.DataEvent;
import com.blue.message.config.blue.BlueProducerConfig;
import com.blue.pulsar.component.BluePulsarProducer;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.PulsarClient;
import reactor.util.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.REQUEST_EVENT;
import static com.blue.pulsar.api.generator.BluePulsarProducerGenerator.generateProducer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static reactor.util.Loggers.getLogger;


/**
 * data report producer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"Duplicates", "JavaDoc"})
public final class RequestEventProducer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(RequestEventProducer.class);

    private final ExecutorService executorService;

    private final BluePulsarProducer<DataEvent> pulsarProducer;

    public RequestEventProducer(PulsarClient pulsarClient, ExecutorService executorService, BlueProducerConfig blueProducerConfig) {
        this.executorService = executorService;
        this.pulsarProducer = generateProducer(pulsarClient, blueProducerConfig.getByKey(REQUEST_EVENT.name), DataEvent.class);
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
     * send event
     *
     * @param dataEvent
     */
    public void send(DataEvent dataEvent) {
        CompletableFuture<MessageId> future = pulsarProducer.sendAsync(dataEvent);

        LOGGER.info("pulsarProducer send, dataEvent = {}", dataEvent);
        Consumer<MessageId> c = messageId ->
                LOGGER.info("pulsarProducer send success, dataEvent = {}, messageId = {}", dataEvent, messageId);

        future.thenAcceptAsync(c, executorService);
    }

}
