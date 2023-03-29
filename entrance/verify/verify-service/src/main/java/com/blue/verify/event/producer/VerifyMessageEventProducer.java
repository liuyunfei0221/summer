package com.blue.verify.event.producer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.pulsar.component.BluePulsarProducer;
import com.blue.verify.api.model.VerifyMessage;
import com.blue.verify.config.blue.BlueProducerConfig;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.PulsarClient;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.VERIFY_MESSAGE;
import static com.blue.pulsar.api.generator.BluePulsarProducerGenerator.generateProducer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static org.slf4j.LoggerFactory.getLogger;


/**
 * marketing event producer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"Duplicates", "JavaDoc"})
public final class VerifyMessageEventProducer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(VerifyMessageEventProducer.class);

    private final ExecutorService executorService;

    private final BluePulsarProducer<VerifyMessage> pulsarProducer;

    public VerifyMessageEventProducer(PulsarClient pulsarClient, ExecutorService executorService, BlueProducerConfig blueProducerConfig) {
        this.executorService = executorService;
        this.pulsarProducer = generateProducer(pulsarClient, blueProducerConfig.getByKey(VERIFY_MESSAGE.name), VerifyMessage.class);
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
     * @param verifyMessage
     */
    public void send(VerifyMessage verifyMessage) {
        CompletableFuture<MessageId> future = pulsarProducer.sendAsync(verifyMessage);

        LOGGER.info("pulsarProducer send, verifyMessage = {}", verifyMessage);
        Consumer<MessageId> c = messageId ->
                LOGGER.info("pulsarProducer send success, verifyMessage = {}, messageId = {}", verifyMessage, messageId);

        future.thenAcceptAsync(c, executorService);
    }

}
