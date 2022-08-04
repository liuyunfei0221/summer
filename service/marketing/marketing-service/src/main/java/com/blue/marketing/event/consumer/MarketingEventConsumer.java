package com.blue.marketing.event.consumer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.exps.BlueException;
import com.blue.marketing.api.model.MarketingEvent;
import com.blue.marketing.config.blue.BlueConsumerConfig;
import com.blue.marketing.service.inter.MarketingEventHandleService;
import com.blue.pulsar.api.generator.BluePulsarListenerGenerator;
import com.blue.pulsar.common.BluePulsarListener;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.MARKETING;
import static com.blue.basic.constant.common.ResponseElement.INTERNAL_SERVER_ERROR;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * marketing event consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class MarketingEventConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(MarketingEventConsumer.class);

    private final BlueConsumerConfig blueConsumerConfig;

    private final Scheduler scheduler;

    private final MarketingEventHandleService marketingEventHandleService;

    private BluePulsarListener<MarketingEvent> pulsarListener;

    public MarketingEventConsumer(BlueConsumerConfig blueConsumerConfig, Scheduler schedule,
                                  MarketingEventHandleService marketingEventHandleService) {
        this.blueConsumerConfig = blueConsumerConfig;
        this.scheduler = schedule;
        this.marketingEventHandleService = marketingEventHandleService;
    }

    @PostConstruct
    private void init() {
        Consumer<MarketingEvent> dataConsumer = marketingEvent ->
                ofNullable(marketingEvent)
                        .ifPresent(me -> just(me).publishOn(scheduler).map(marketingEventHandleService::handleEvent)
                                .switchIfEmpty(defer(() -> error(() -> new BlueException(INTERNAL_SERVER_ERROR))))
                                .doOnError(throwable -> LOGGER.info("marketingEventHandleService.handleEvent(me) failed, me = {}, throwable = {}", me, throwable))
                                .subscribe(er -> LOGGER.info("marketingEventHandleService.handleEvent(me), er = {}, me = {}", er, me)));

        this.pulsarListener = BluePulsarListenerGenerator.generateListener(blueConsumerConfig.getByKey(MARKETING.name), dataConsumer);
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
