package com.blue.shine.event.producer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.pulsar.component.BluePulsarProducer;
import com.blue.shine.config.blue.BlueProducerConfig;
import com.blue.shine.repository.entity.Shine;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.PulsarClient;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.SHINE_UPDATE;
import static com.blue.pulsar.api.generator.BluePulsarProducerGenerator.generateProducer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * shine update producer
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public final class ShineUpdateProducer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(ShineUpdateProducer.class);

    private final ExecutorService executorService;

    private final BluePulsarProducer<Shine> pulsarProducer;

    public ShineUpdateProducer(PulsarClient pulsarClient, ExecutorService executorService, BlueProducerConfig blueProducerConfig) {
        this.executorService = executorService;
        this.pulsarProducer = generateProducer(pulsarClient, blueProducerConfig.getByKey(SHINE_UPDATE.name), Shine.class);
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
     * @param shine
     */
    public void send(Shine shine) {
        CompletableFuture<MessageId> future = pulsarProducer.sendAsync(shine);
        LOGGER.info("pulsarProducer send, shine = {}", shine);
        Consumer<MessageId> c = messageId ->
                LOGGER.info("pulsarProducer send success, shine = {}, messageId = {}", shine, messageId);
        future.thenAcceptAsync(c, executorService);
    }

}
