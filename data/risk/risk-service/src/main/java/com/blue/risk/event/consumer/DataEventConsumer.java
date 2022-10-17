package com.blue.risk.event.consumer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.event.DataEvent;
import com.blue.basic.model.exps.BlueException;
import com.blue.pulsar.api.generator.BluePulsarListenerGenerator;
import com.blue.pulsar.component.BluePulsarListener;
import com.blue.risk.config.blue.BlueConsumerConfig;
import com.blue.risk.service.inter.RiskService;
import org.apache.pulsar.client.api.PulsarClient;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.REQUEST_EVENT;
import static com.blue.basic.constant.common.ResponseElement.INTERNAL_SERVER_ERROR;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * data event consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public final class DataEventConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(DataEventConsumer.class);

    private final PulsarClient pulsarClient;

    private final BlueConsumerConfig blueConsumerConfig;

    private final Scheduler scheduler;

    private final RiskService riskService;

    private BluePulsarListener<DataEvent> pulsarListener;

    public DataEventConsumer(PulsarClient pulsarClient, BlueConsumerConfig blueConsumerConfig, Scheduler scheduler, RiskService riskService) {
        this.pulsarClient = pulsarClient;
        this.blueConsumerConfig = blueConsumerConfig;
        this.scheduler = scheduler;
        this.riskService = riskService;
    }

    @PostConstruct
    private void init() {
        Consumer<DataEvent> dataConsumer = dataEvent ->
                ofNullable(dataEvent)
                        .ifPresent(de -> just(de).publishOn(scheduler).flatMap(riskService::analyzeEvent)
                                .switchIfEmpty(defer(() -> error(() -> new BlueException(INTERNAL_SERVER_ERROR))))
                                .doOnError(throwable -> LOGGER.info("riskService.analyzeEvent(de) failed, de = {}, throwable = {}", de, throwable))
                                .subscribe(b -> LOGGER.info("riskService.analyzeEvent(de), b = {}, de = {}", b, de)));

        this.pulsarListener = BluePulsarListenerGenerator.generateListener(pulsarClient, blueConsumerConfig.getByKey(REQUEST_EVENT.name), dataConsumer);
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
