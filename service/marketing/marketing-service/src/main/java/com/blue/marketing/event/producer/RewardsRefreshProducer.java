package com.blue.marketing.event.producer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.event.EmptyEvent;
import com.blue.marketing.config.blue.BlueProducerConfig;
import com.blue.pulsar.component.BluePulsarProducer;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.PulsarClient;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.REWARDS_REFRESH;
import static com.blue.pulsar.api.generator.BluePulsarProducerGenerator.generateProducer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * refresh rewards producer
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public final class RewardsRefreshProducer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(RewardsRefreshProducer.class);

    private final ExecutorService executorService;

    private final BluePulsarProducer<EmptyEvent> pulsarProducer;

    public RewardsRefreshProducer(PulsarClient pulsarClient, ExecutorService executorService, BlueProducerConfig blueProducerConfig) {
        this.executorService = executorService;
        this.pulsarProducer = generateProducer(pulsarClient, blueProducerConfig.getByKey(REWARDS_REFRESH.name), EmptyEvent.class);
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
     * @param emptyEvent
     */
    public void send(EmptyEvent emptyEvent) {
        CompletableFuture<MessageId> future = pulsarProducer.sendAsync(emptyEvent);
        LOGGER.info("pulsarProducer send, nonValueParam = {}", emptyEvent);
        Consumer<MessageId> c = messageId ->
                LOGGER.info("pulsarProducer send success, nonValueParam = {}, messageId = {}", emptyEvent, messageId);
        future.thenAcceptAsync(c, executorService);
    }

}
