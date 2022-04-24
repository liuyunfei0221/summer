package com.blue.risk.event.consumer;

import com.blue.base.component.lifecycle.inter.BlueLifecycle;
import com.blue.base.model.base.DataEvent;
import com.blue.pulsar.common.BluePulsarConsumer;
import com.blue.risk.config.blue.BlueConsumerConfig;
import com.blue.risk.service.inter.RiskService;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.base.constant.base.BlueTopic.REQUEST_EVENT;
import static com.blue.pulsar.api.generator.BluePulsarConsumerGenerator.generateConsumer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * data event consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public final class DataEventConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(DataEventConsumer.class);

    private final RiskService riskService;

    private final BlueConsumerConfig blueConsumerConfig;

    private BluePulsarConsumer<DataEvent> dataEventConsumer;

    public DataEventConsumer(RiskService riskService, BlueConsumerConfig blueConsumerConfig) {
        this.riskService = riskService;
        this.blueConsumerConfig = blueConsumerConfig;
    }

    @PostConstruct
    private void init() {
        Consumer<DataEvent> dataEventDataConsumer = dataEvent ->
                ofNullable(dataEvent)
                        .ifPresent(de -> {
                            LOGGER.info("dataEventDataConsumer received");
                            riskService.analyzeEvent(de)
                                    .onErrorResume(throwable -> {
                                        LOGGER.error("riskService.analyzeEvent(de) failed, de = {}, throwable = {}", de, throwable);
                                        return just(true);
                                    }).toFuture().join();

                            LOGGER.info("Mono<Void> analyzeEvent(DataEvent dataEvent), de = {}", de);
                        });

        this.dataEventConsumer = generateConsumer(blueConsumerConfig.getByKey(REQUEST_EVENT.name), dataEventDataConsumer);
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
        this.dataEventConsumer.run();
        LOGGER.warn("dataEventConsumer start...");
    }

    @Override
    public void stop() {
        this.dataEventConsumer.shutdown();
        LOGGER.warn("dataEventConsumer shutdown...");
    }

}
