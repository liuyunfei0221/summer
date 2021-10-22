package com.blue.marketing.event.producer;

import com.blue.base.component.lifecycle.inter.BlueLifecycle;
import com.blue.marketing.api.model.MarketingEvent;
import com.blue.marketing.config.blue.BlueProducerConfig;
import com.blue.pulsar.common.BluePulsarProducer;
import org.apache.pulsar.client.api.MessageId;
import reactor.util.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.base.constant.base.BlueTopic.MARKETING;
import static com.blue.pulsar.api.generator.BluePulsarProducerGenerator.generateProducer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static reactor.util.Loggers.getLogger;


/**
 * marketing event producer
 *
 * @author DarkBlue
 */
@SuppressWarnings({"Duplicates", "JavaDoc"})
public class MarketingEventProducer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(MarketingEventProducer.class);

    private final ExecutorService executorService;

    private final BluePulsarProducer<MarketingEvent> marketingEventProducer;

    public MarketingEventProducer(ExecutorService executorService, BlueProducerConfig blueProducerConfig) {
        this.executorService = executorService;
        this.marketingEventProducer = generateProducer(blueProducerConfig.getByKey(MARKETING.name), MarketingEvent.class);
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
        LOGGER.warn("illegalMarkProducer start...");
    }

    @Override
    public void stop() {
        this.marketingEventProducer.shutdown();
        LOGGER.warn("marketingEventProducer shutdown...");
    }

    /**
     * 发送消息
     *
     * @param marketingEvent
     */
    public void send(MarketingEvent marketingEvent) {
        CompletableFuture<MessageId> future = marketingEventProducer.sendAsync(marketingEvent);

        LOGGER.info("marketingEventProducer send, marketingEvent = {}", marketingEvent);
        Consumer<MessageId> c = messageId ->
                LOGGER.info("marketingEventProducer send success, marketingEvent = {}, messageId = {}", marketingEvent, messageId);

        future.thenAcceptAsync(c, executorService);
    }

}
