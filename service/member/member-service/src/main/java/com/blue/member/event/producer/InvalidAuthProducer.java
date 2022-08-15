package com.blue.member.event.producer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.event.InvalidAuthEvent;
import com.blue.member.config.blue.BlueProducerConfig;
import com.blue.pulsar.common.BluePulsarProducer;
import org.apache.pulsar.client.api.MessageId;
import reactor.util.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.INVALID_AUTH;
import static com.blue.pulsar.api.generator.BluePulsarProducerGenerator.generateProducer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static reactor.util.Loggers.getLogger;

/**
 * invalid auth producer
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public final class InvalidAuthProducer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(InvalidAuthProducer.class);

    private final ExecutorService executorService;

    private final BluePulsarProducer<InvalidAuthEvent> pulsarProducer;

    public InvalidAuthProducer(ExecutorService executorService, BlueProducerConfig blueProducerConfig) {
        this.executorService = executorService;
        this.pulsarProducer = generateProducer(blueProducerConfig.getByKey(INVALID_AUTH.name), InvalidAuthEvent.class);
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
     * @param invalidAuthEvent
     */
    public void send(InvalidAuthEvent invalidAuthEvent) {
        CompletableFuture<MessageId> future = pulsarProducer.sendAsync(invalidAuthEvent);
        LOGGER.info("pulsarProducer send, pulsarProducer = {}", invalidAuthEvent);
        Consumer<MessageId> c = messageId ->
                LOGGER.info("pulsarProducer send success, invalidAuthEvent = {}, messageId = {}", invalidAuthEvent, messageId);
        future.thenAcceptAsync(c, executorService);
    }

}
