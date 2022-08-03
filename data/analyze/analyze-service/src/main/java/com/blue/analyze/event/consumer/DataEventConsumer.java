package com.blue.analyze.event.consumer;

import com.blue.analyze.component.statistics.StatisticsProcessor;
import com.blue.analyze.config.blue.BlueConsumerConfig;
import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.event.DataEvent;
import com.blue.basic.model.exps.BlueException;
import com.blue.pulsar.api.generator.BluePulsarListenerGenerator;
import com.blue.pulsar.common.BluePulsarListener;
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

    private final BlueConsumerConfig blueConsumerConfig;

    private final Scheduler scheduler;

    private final StatisticsProcessor statisticsProcessor;

    private BluePulsarListener<DataEvent> dataEventConsumer;

    public DataEventConsumer(BlueConsumerConfig blueConsumerConfig, Scheduler scheduler, StatisticsProcessor statisticsProcessor) {
        this.blueConsumerConfig = blueConsumerConfig;
        this.scheduler = scheduler;
        this.statisticsProcessor = statisticsProcessor;
    }

    @PostConstruct
    private void init() {
        Consumer<DataEvent> dataEventDataConsumer = dataEvent ->
                ofNullable(dataEvent)
                        .ifPresent(de -> just(de).publishOn(scheduler).map(statisticsProcessor::process)
                                .switchIfEmpty(defer(() -> error(() -> new BlueException(INTERNAL_SERVER_ERROR))))
                                .doOnError(throwable -> LOGGER.info("statisticsProcessor.process(de) failed, de = {}, throwable = {}", de, throwable))
                                .subscribe(b -> LOGGER.info("statisticsProcessor.process(de), b = {}, de = {}", b, de)));

        this.dataEventConsumer = BluePulsarListenerGenerator.generateListener(blueConsumerConfig.getByKey(REQUEST_EVENT.name), dataEventDataConsumer);
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
