package com.blue.shine.event.consumer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.exps.BlueException;
import com.blue.pulsar.common.BluePulsarListener;
import com.blue.shine.config.blue.BlueConsumerConfig;
import com.blue.shine.repository.entity.Shine;
import com.blue.shine.service.inter.ShineService;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.SHINE_UPDATE;
import static com.blue.basic.constant.common.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.pulsar.api.generator.BluePulsarListenerGenerator.generateListener;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;


/**
 * shine update consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public final class ShineUpdateConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(ShineUpdateConsumer.class);

    private final BlueConsumerConfig blueConsumerConfig;

    private final Scheduler scheduler;

    private final ShineService shineService;

    private BluePulsarListener<Shine> pulsarListener;

    public ShineUpdateConsumer(BlueConsumerConfig blueConsumerConfig, Scheduler scheduler, ShineService shineService) {
        this.blueConsumerConfig = blueConsumerConfig;
        this.scheduler = scheduler;
        this.shineService = shineService;
    }

    @PostConstruct
    private void init() {
        Consumer<Shine> dataConsumer = shine ->
                ofNullable(shine)
                        .ifPresent(s -> just(s).publishOn(scheduler).map(shineService::updateShineEvent)
                                .switchIfEmpty(defer(() -> error(() -> new BlueException(INTERNAL_SERVER_ERROR))))
                                .doOnError(throwable -> LOGGER.info("shineService.updateShineEvent(s) failed, s = {}, throwable = {}", s, throwable))
                                .subscribe(b -> LOGGER.info("shineService.updateShineEvent(s), b = {}, s = {}", b, s)));

        this.pulsarListener = generateListener(blueConsumerConfig.getByKey(SHINE_UPDATE.name), dataConsumer);
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
