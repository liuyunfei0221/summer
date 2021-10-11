package com.blue.marketing.config.mq.consumer;

import com.blue.base.component.lifecycle.inter.BlueLifecycle;
import com.blue.base.constant.base.BlueTopic;
import com.blue.marketing.api.model.EventHandleResult;
import com.blue.marketing.api.model.MarketingEvent;
import com.blue.marketing.config.blue.BlueConsumerConfig;
import com.blue.marketing.service.inter.MarketingEventHandleService;
import com.blue.pulsar.api.conf.ConsumerConfParams;
import com.blue.pulsar.common.BluePulsarConsumer;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.pulsar.api.generator.BluePulsarConsumerGenerator.generateConsumer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * marketing event consumer
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class MarketingConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(MarketingConsumer.class);

    private final MarketingEventHandleService marketingEventHandleService;

    private final BlueConsumerConfig blueConsumerConfig;

    private BluePulsarConsumer<MarketingEvent> marketingConsumer;

    public MarketingConsumer(MarketingEventHandleService marketingEventHandleService, BlueConsumerConfig blueConsumerConfig) {
        this.marketingEventHandleService = marketingEventHandleService;
        this.blueConsumerConfig = blueConsumerConfig;
    }

    @PostConstruct
    private void init() {
        Consumer<MarketingEvent> marketingDataConsumer = marketingEvent ->
                ofNullable(marketingEvent)
                        .ifPresent(me -> {
                            LOGGER.info("marketingDataConsumer received, me = {}", me);
                            EventHandleResult eventHandleResult = marketingEventHandleService.handleEvent(me);
                            LOGGER.info("marketingEventHandleService.handleEvent(marketingEvent), me = {}. eventHandleResult = {}",
                                    me, eventHandleResult);
                        });

        ConsumerConfParams marketingDeploy = blueConsumerConfig.getByKey(BlueTopic.MARKETING.name);
        this.marketingConsumer = generateConsumer(marketingDeploy, marketingDataConsumer);
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
