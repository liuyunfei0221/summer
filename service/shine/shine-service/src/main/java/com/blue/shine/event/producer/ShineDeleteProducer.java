package com.blue.shine.event.producer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.event.IdentityEvent;
import com.blue.pulsar.component.BluePulsarProducer;
import com.blue.shine.config.blue.BlueProducerConfig;
import org.apache.pulsar.client.api.MessageId;
import reactor.util.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.SHINE_DELETE;
import static com.blue.pulsar.api.generator.BluePulsarProducerGenerator.generateProducer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static reactor.util.Loggers.getLogger;

/**
 * shine delete producer
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public final class ShineDeleteProducer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(ShineDeleteProducer.class);

    private final ExecutorService executorService;

    private final BluePulsarProducer<IdentityEvent> pulsarProducer;

    public ShineDeleteProducer(ExecutorService executorService, BlueProducerConfig blueProducerConfig) {
        this.executorService = executorService;
        this.pulsarProducer = generateProducer(blueProducerConfig.getByKey(SHINE_DELETE.name), IdentityEvent.class);
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
     * @param identityEvent
     */
    public void send(IdentityEvent identityEvent) {
        CompletableFuture<MessageId> future = pulsarProducer.sendAsync(identityEvent);
        LOGGER.info("pulsarProducer send, identityEvent = {}", identityEvent);
        Consumer<MessageId> c = messageId ->
                LOGGER.info("pulsarProducer send success, identityEvent = {}, messageId = {}", identityEvent, messageId);
        future.thenAcceptAsync(c, executorService);
    }

}
