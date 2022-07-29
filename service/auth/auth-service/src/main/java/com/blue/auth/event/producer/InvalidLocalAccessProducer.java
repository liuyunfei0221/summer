package com.blue.auth.event.producer;

import com.blue.auth.config.blue.BlueProducerConfig;
import com.blue.auth.event.model.InvalidLocalAccessEvent;
import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.pulsar.common.BluePulsarProducer;
import org.apache.pulsar.client.api.MessageId;
import reactor.util.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.INVALID_LOCAL_ACCESS;
import static com.blue.pulsar.api.generator.BluePulsarProducerGenerator.generateProducer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static reactor.util.Loggers.getLogger;

/**
 * invalid auth from local cache producer
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public final class InvalidLocalAccessProducer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(InvalidLocalAccessProducer.class);

    private final ExecutorService executorService;

    private final BluePulsarProducer<InvalidLocalAccessEvent> invalidLocalAccessProducer;

    public InvalidLocalAccessProducer(ExecutorService executorService, BlueProducerConfig blueProducerConfig) {
        this.executorService = executorService;
        this.invalidLocalAccessProducer = generateProducer(blueProducerConfig.getByKey(INVALID_LOCAL_ACCESS.name), InvalidLocalAccessEvent.class);
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
        this.invalidLocalAccessProducer.flush();
        this.invalidLocalAccessProducer.close();
        LOGGER.warn("invalidLocalAccessProducer shutdown...");
    }

    /**
     * send message
     *
     * @param invalidLocalAccessEvent
     */
    public void send(InvalidLocalAccessEvent invalidLocalAccessEvent) {
        CompletableFuture<MessageId> future = invalidLocalAccessProducer.sendAsync(invalidLocalAccessEvent);
        LOGGER.info("invalidLocalAccessProducer send, invalidLocalAccessEvent = {}", invalidLocalAccessEvent);
        Consumer<MessageId> c = messageId ->
                LOGGER.info("invalidLocalAccessProducer send success, invalidLocalAccessEvent = {}, messageId = {}", invalidLocalAccessEvent, messageId);
        future.thenAcceptAsync(c, executorService);
    }

}
