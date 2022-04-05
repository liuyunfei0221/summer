package com.blue.auth.event.producer;

import com.blue.auth.api.model.InvalidLocalAuthParam;
import com.blue.auth.config.blue.BlueProducerConfig;
import com.blue.base.component.lifecycle.inter.BlueLifecycle;
import com.blue.pulsar.common.BluePulsarProducer;
import org.apache.pulsar.client.api.MessageId;
import reactor.util.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.base.constant.base.BlueTopic.INVALID_LOCAL_ACCESS;
import static com.blue.pulsar.api.generator.BluePulsarProducerGenerator.generateProducer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static reactor.util.Loggers.getLogger;

/**
 * invalid auth from local cache consumer
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public final class InvalidLocalAccessProducer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(InvalidLocalAccessProducer.class);

    private final ExecutorService executorService;

    private final BluePulsarProducer<InvalidLocalAuthParam> invalidLocalAuthProducer;

    public InvalidLocalAccessProducer(ExecutorService executorService, BlueProducerConfig blueProducerConfig) {
        this.executorService = executorService;
        this.invalidLocalAuthProducer = generateProducer(blueProducerConfig.getByKey(INVALID_LOCAL_ACCESS.name), InvalidLocalAuthParam.class);
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
        LOGGER.warn("invalidLocalAuthProducer start...");
    }

    @Override
    public void stop() {
        this.invalidLocalAuthProducer.shutdown();
        LOGGER.warn("invalidLocalAuthProducer shutdown...");
    }

    /**
     * send message
     *
     * @param invalidLocalAuthParam
     */
    public void send(InvalidLocalAuthParam invalidLocalAuthParam) {
        CompletableFuture<MessageId> future = invalidLocalAuthProducer.sendAsync(invalidLocalAuthParam);
        LOGGER.info("invalidLocalAuthProducer send, invalidLocalAuthParam = {}", invalidLocalAuthParam);
        Consumer<MessageId> c = messageId ->
                LOGGER.info("invalidLocalAuthProducer send success, invalidLocalAuthParam = {}, messageId = {}", invalidLocalAuthParam, messageId);
        future.thenAcceptAsync(c, executorService);
    }

}
