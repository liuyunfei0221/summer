package com.blue.marketing.event.producer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.common.EmptyEvent;
import com.blue.marketing.config.blue.BlueProducerConfig;
import com.blue.pulsar.common.BluePulsarProducer;
import org.apache.pulsar.client.api.MessageId;
import reactor.util.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.REWARDS_REFRESH;
import static com.blue.pulsar.api.generator.BluePulsarProducerGenerator.generateProducer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static reactor.util.Loggers.getLogger;

/**
 * refresh rewards producer
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public final class RewardsRefreshProducer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(RewardsRefreshProducer.class);

    private final ExecutorService executorService;

    private final BluePulsarProducer<EmptyEvent> rewardsRefreshProducer;

    public RewardsRefreshProducer(ExecutorService executorService, BlueProducerConfig blueProducerConfig) {
        this.executorService = executorService;
        this.rewardsRefreshProducer = generateProducer(blueProducerConfig.getByKey(REWARDS_REFRESH.name), EmptyEvent.class);
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
        LOGGER.warn("rewardsRefreshProducer start...");
    }

    @Override
    public void stop() {
        this.rewardsRefreshProducer.shutdown();
        LOGGER.warn("rewardsRefreshProducer shutdown...");
    }

    /**
     * send message
     *
     * @param emptyEvent
     */
    public void send(EmptyEvent emptyEvent) {
        CompletableFuture<MessageId> future = rewardsRefreshProducer.sendAsync(emptyEvent);
        LOGGER.info("rewardsRefreshProducer send, nonValueParam = {}", emptyEvent);
        Consumer<MessageId> c = messageId ->
                LOGGER.info("rewardsRefreshProducer send success, nonValueParam = {}, messageId = {}", emptyEvent, messageId);
        future.thenAcceptAsync(c, executorService);
    }

}
