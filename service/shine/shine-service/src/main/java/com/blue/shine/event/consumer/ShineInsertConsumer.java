package com.blue.shine.event.consumer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.exps.BlueException;
import com.blue.pulsar.component.BluePulsarListener;
import com.blue.shine.config.blue.BlueConsumerConfig;
import com.blue.shine.repository.entity.Shine;
import com.blue.shine.service.inter.ShineService;
import org.apache.pulsar.client.api.PulsarClient;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.SHINE_INSERT;
import static com.blue.basic.constant.common.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.pulsar.api.generator.BluePulsarListenerGenerator.generateListener;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;


/**
 * shine insert consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public final class ShineInsertConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(ShineInsertConsumer.class);

    private final PulsarClient pulsarClient;

    private final BlueConsumerConfig blueConsumerConfig;

    private final Scheduler scheduler;

    private final ShineService shineService;

    private BluePulsarListener<Shine> pulsarListener;

    public ShineInsertConsumer(PulsarClient pulsarClient, BlueConsumerConfig blueConsumerConfig, Scheduler scheduler, ShineService shineService) {
        this.pulsarClient = pulsarClient;
        this.blueConsumerConfig = blueConsumerConfig;
        this.scheduler = scheduler;
        this.shineService = shineService;
    }

    @PostConstruct
    private void init() {
        Consumer<Shine> dataConsumer = shine ->
                ofNullable(shine)
                        .ifPresent(s -> just(s).publishOn(scheduler).flatMap(shineService::insertShineEvent)
                                .switchIfEmpty(defer(() -> error(() -> new BlueException(INTERNAL_SERVER_ERROR))))
                                .doOnError(throwable -> LOGGER.info("shineService.insertShineEvent(s) failed, s = {}, throwable = {}", s, throwable))
                                .subscribe(b -> LOGGER.info("shineService.insertShineEvent(s), b = {}, s = {}", b, s)));

        this.pulsarListener = generateListener(pulsarClient, blueConsumerConfig.getByKey(SHINE_INSERT.name), dataConsumer);
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
