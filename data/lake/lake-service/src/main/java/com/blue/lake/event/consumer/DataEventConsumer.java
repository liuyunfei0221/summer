package com.blue.lake.event.consumer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.event.DataEvent;
import com.blue.basic.model.exps.BlueException;
import com.blue.lake.config.blue.BlueConsumerConfig;
import com.blue.lake.service.inter.LakeService;
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

    private final LakeService lakeService;

    private BluePulsarListener<DataEvent> dataEventConsumer;

    public DataEventConsumer(BlueConsumerConfig blueConsumerConfig, Scheduler scheduler, LakeService lakeService) {
        this.blueConsumerConfig = blueConsumerConfig;
        this.scheduler = scheduler;
        this.lakeService = lakeService;
    }

    @PostConstruct
    private void init() {
        Consumer<DataEvent> dataEventDataConsumer = dataEvent ->
                ofNullable(dataEvent)
                        .ifPresent(de -> just(de).publishOn(scheduler).map(lakeService::insertEvent)
                                .switchIfEmpty(defer(() -> error(() -> new BlueException(INTERNAL_SERVER_ERROR))))
                                .doOnError(throwable -> LOGGER.info("insertEvent(DataEvent de) failed, de = {}, throwable = {}", de, throwable))
                                .subscribe(b -> LOGGER.info("insertEvent(DataEvent de), b = {}, de = {}", b, de)));

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
