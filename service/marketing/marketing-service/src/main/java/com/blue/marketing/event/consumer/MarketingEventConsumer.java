package com.blue.marketing.event.consumer;

import com.blue.base.component.lifecycle.inter.BlueLifecycle;
import com.blue.marketing.api.model.MarketingEvent;
import com.blue.marketing.config.blue.BlueConsumerConfig;
import com.blue.marketing.repository.entity.EventRecord;
import com.blue.marketing.service.inter.MarketingEventHandleService;
import com.blue.pulsar.common.BluePulsarConsumer;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.base.constant.base.BlueTopic.MARKETING;
import static com.blue.pulsar.api.generator.BluePulsarConsumerGenerator.generateConsumer;
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

    private final MarketingEventHandleService marketingEventHandleService;

    private final BlueConsumerConfig blueConsumerConfig;

    private BluePulsarConsumer<MarketingEvent> marketingConsumer;

    public MarketingEventConsumer(MarketingEventHandleService marketingEventHandleService, BlueConsumerConfig blueConsumerConfig) {
        this.marketingEventHandleService = marketingEventHandleService;
        this.blueConsumerConfig = blueConsumerConfig;
    }

    @PostConstruct
    private void init() {
        Consumer<MarketingEvent> marketingDataConsumer = marketingEvent ->
                ofNullable(marketingEvent)
                        .ifPresent(me -> {
                            EventRecord eventRecord = marketingEventHandleService.handleEvent(me);
                            LOGGER.info("marketingEventHandleService.handleEvent(marketingEvent), me = {}. eventRecord = {}", me, eventRecord);
                        });

        this.marketingConsumer = generateConsumer(blueConsumerConfig.getByKey(MARKETING.name), marketingDataConsumer);
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
        this.marketingConsumer.run();
        LOGGER.warn("marketingDataConsumer start...");
    }

    @Override
    public void stop() {
        this.marketingConsumer.shutdown();
        LOGGER.warn("marketingDataConsumer shutdown...");
    }

}
