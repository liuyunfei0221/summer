package com.blue.marketing.event.producer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.marketing.api.model.MarketingEvent;
import com.blue.marketing.config.blue.BlueProducerConfig;
import com.blue.pulsar.component.BluePulsarProducer;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.PulsarClient;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.MARKETING;
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
public final class MarketingEventProducer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(MarketingEventProducer.class);

    private final ExecutorService executorService;

    private final BluePulsarProducer<MarketingEvent> pulsarProducer;

    public MarketingEventProducer(PulsarClient pulsarClient, ExecutorService executorService, BlueProducerConfig blueProducerConfig) {
        this.executorService = executorService;
        this.pulsarProducer = generateProducer(pulsarClient, blueProducerConfig.getByKey(MARKETING.name), MarketingEvent.class);
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
     * @param marketingEvent
     */
    public void send(MarketingEvent marketingEvent) {
        CompletableFuture<MessageId> future = pulsarProducer.sendAsync(marketingEvent);

        LOGGER.info("pulsarProducer send, marketingEvent = {}", marketingEvent);
        Consumer<MessageId> c = messageId ->
                LOGGER.info("pulsarProducer send success, marketingEvent = {}, messageId = {}", marketingEvent, messageId);

        future.thenAcceptAsync(c, executorService);
    }

}
