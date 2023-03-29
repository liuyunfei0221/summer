package com.blue.auth.event.producer;

import com.blue.auth.api.model.InvalidLocalAccessEvent;
import com.blue.auth.config.blue.BlueProducerConfig;
import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.pulsar.component.BluePulsarProducer;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.PulsarClient;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.INVALID_LOCAL_ACCESS;
import static com.blue.pulsar.api.generator.BluePulsarProducerGenerator.generateProducer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * invalid auth from local cache producer
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public final class InvalidLocalAccessProducer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(InvalidLocalAccessProducer.class);

    private final ExecutorService executorService;

    private final BluePulsarProducer<InvalidLocalAccessEvent> pulsarProducer;

    public InvalidLocalAccessProducer(PulsarClient pulsarClient, ExecutorService executorService, BlueProducerConfig blueProducerConfig) {
        this.executorService = executorService;
        this.pulsarProducer = generateProducer(pulsarClient, blueProducerConfig.getByKey(INVALID_LOCAL_ACCESS.name), InvalidLocalAccessEvent.class);
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
     * @param invalidLocalAccessEvent
     */
    public void send(InvalidLocalAccessEvent invalidLocalAccessEvent) {
        CompletableFuture<MessageId> future = pulsarProducer.sendAsync(invalidLocalAccessEvent);
        LOGGER.info("pulsarProducer send, invalidLocalAccessEvent = {}", invalidLocalAccessEvent);
        Consumer<MessageId> c = messageId ->
                LOGGER.info("pulsarProducer send success, invalidLocalAccessEvent = {}, messageId = {}", invalidLocalAccessEvent, messageId);
        future.thenAcceptAsync(c, executorService);
    }

}
