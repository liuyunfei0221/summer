package com.blue.secure.config.mq.producer;//package com.blue.secure.config.mq.api.producer;

import com.blue.base.component.lifecycle.inter.BlueLifecycle;
import com.blue.base.constant.base.BlueTopic;
import com.blue.pulsar.common.BluePulsarProducer;
import com.blue.secure.api.model.InvalidLocalAuthParam;
import com.blue.secure.config.blue.BlueProducerConfig;
import org.apache.pulsar.client.api.MessageId;
import reactor.util.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

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
public final class InvalidLocalAuthProducer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(InvalidLocalAuthProducer.class);

    private final ExecutorService executorService;

    private final BluePulsarProducer<InvalidLocalAuthParam> invalidLocalAuthProducer;

    public InvalidLocalAuthProducer(ExecutorService executorService, BlueProducerConfig blueProducerConfig) {
        this.executorService = executorService;
        this.invalidLocalAuthProducer = generateProducer(blueProducerConfig.getByKey(BlueTopic.INVALID_LOCAL_AUTH.name), InvalidLocalAuthParam.class);
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
     * 发送消息
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
