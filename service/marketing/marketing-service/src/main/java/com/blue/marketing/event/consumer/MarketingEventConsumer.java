package com.blue.marketing.event.consumer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.marketing.api.model.MarketingEvent;
import com.blue.marketing.config.blue.BlueConsumerConfig;
import com.blue.marketing.service.inter.MarketingEventHandleService;
import com.blue.pulsar.api.generator.BluePulsarListenerGenerator;
import com.blue.pulsar.component.BluePulsarListener;
import org.apache.pulsar.client.api.PulsarClient;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.MARKETING;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * marketing event consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class MarketingEventConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(MarketingEventConsumer.class);

    private final PulsarClient pulsarClient;

    private final BlueConsumerConfig blueConsumerConfig;

    private final MarketingEventHandleService marketingEventHandleService;

    private BluePulsarListener<MarketingEvent> pulsarListener;

    public MarketingEventConsumer(PulsarClient pulsarClient, BlueConsumerConfig blueConsumerConfig,
                                  MarketingEventHandleService marketingEventHandleService) {
        this.pulsarClient = pulsarClient;
        this.blueConsumerConfig = blueConsumerConfig;
        this.marketingEventHandleService = marketingEventHandleService;
    }

    @PostConstruct
    private void init() {
        Consumer<MarketingEvent> dataConsumer = marketingEvent ->
                ofNullable(marketingEvent)
                        .ifPresent(marketingEventHandleService::handleEvent);

        this.pulsarListener = BluePulsarListenerGenerator.generateListener(pulsarClient, blueConsumerConfig.getByKey(MARKETING.name), dataConsumer);
    }

    @Override
    public int startPrecedence() {
        return MAX_VALUE;
    }

    @Override
    public int stopPrecedence() {
        return MIN_VALUE;
    }

    @Override
    public void start() {
        this.pulsarListener.run();
        LOGGER.warn("pulsarListener start...");
    }

    @Override
    public void stop() {
        this.pulsarListener.shutdown();
        LOGGER.warn("pulsarListener shutdown...");
    }

}
