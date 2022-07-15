package com.blue.auth.event.producer;

import com.blue.auth.config.blue.BlueProducerConfig;
import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.common.KeyExpireEvent;
import com.blue.pulsar.common.BluePulsarProducer;
import org.apache.pulsar.client.api.MessageId;
import reactor.util.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.ACCESS_EXPIRE;
import static com.blue.pulsar.api.generator.BluePulsarProducerGenerator.generateProducer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static reactor.util.Loggers.getLogger;


/**
 * access expire producer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"Duplicates", "JavaDoc"})
public final class AccessExpireProducer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(AccessExpireProducer.class);

    private final ExecutorService executorService;

    private final BluePulsarProducer<KeyExpireEvent> authExpireProducer;

    public AccessExpireProducer(ExecutorService executorService, BlueProducerConfig blueProducerConfig) {
        this.executorService = executorService;
        this.authExpireProducer = generateProducer(blueProducerConfig.getByKey(ACCESS_EXPIRE.name), KeyExpireEvent.class);
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
        LOGGER.warn("accessExpireProducer start...");
    }

    @Override
    public void stop() {
        this.authExpireProducer.shutdown();
        LOGGER.warn("accessExpireProducer shutdown...");
    }

    /**
     * send message
     *
     * @param keyExpireEvent
     */
    public void send(KeyExpireEvent keyExpireEvent) {
        CompletableFuture<MessageId> future = authExpireProducer.sendAsync(keyExpireEvent);
        LOGGER.info("authExpireProducer send, keyExpireEvent = {}", keyExpireEvent);
        Consumer<MessageId> c = messageId ->
                LOGGER.info("authExpireProducer send success, keyExpireEvent = {}, messageId = {}", keyExpireEvent, messageId);
        future.thenAcceptAsync(c, executorService);
    }

}
