package com.blue.base.event.producer;

import com.blue.base.component.lifecycle.inter.BlueLifecycle;
import com.blue.base.config.blue.BlueProducerConfig;
import com.blue.base.model.common.EmptyEvent;
import com.blue.pulsar.common.BluePulsarProducer;
import org.apache.pulsar.client.api.MessageId;
import reactor.util.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.base.constant.base.BlueTopic.REGION_INFOS_INVALID;
import static com.blue.pulsar.api.generator.BluePulsarProducerGenerator.generateProducer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static reactor.util.Loggers.getLogger;

/**
 * invalid region info producer
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public final class RegionInfosInvalidProducer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(RegionInfosInvalidProducer.class);

    private final ExecutorService executorService;

    private final BluePulsarProducer<EmptyEvent> regionInfosInvalidProducer;

    public RegionInfosInvalidProducer(ExecutorService executorService, BlueProducerConfig blueProducerConfig) {
        this.executorService = executorService;
        this.regionInfosInvalidProducer = generateProducer(blueProducerConfig.getByKey(REGION_INFOS_INVALID.name), EmptyEvent.class);
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
        LOGGER.warn("regionInfosInvalidProducer start...");
    }

    @Override
    public void stop() {
        this.regionInfosInvalidProducer.shutdown();
        LOGGER.warn("regionInfosInvalidProducer shutdown...");
    }

    /**
     * send message
     *
     * @param emptyEvent
     */
    public void send(EmptyEvent emptyEvent) {
        CompletableFuture<MessageId> future = regionInfosInvalidProducer.sendAsync(emptyEvent);
        LOGGER.info("regionInfosInvalidProducer send");
        Consumer<MessageId> c = messageId ->
                LOGGER.info("regionInfosInvalidProducer send success, messageId = {}", messageId);
        future.thenAcceptAsync(c, executorService);
    }

}
