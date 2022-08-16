package com.blue.base.event.consumer;

import com.blue.base.config.blue.BlueConsumerConfig;
import com.blue.base.service.inter.RegionControlService;
import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.event.EmptyEvent;
import com.blue.pulsar.component.BluePulsarListener;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.REGION_INFOS_INVALID;
import static com.blue.pulsar.api.generator.BluePulsarListenerGenerator.generateListener;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * invalid region info consumer
 *
 * @author liuyunfei
 */
public final class RegionInfosInvalidConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(RegionInfosInvalidConsumer.class);

    private final BlueConsumerConfig blueConsumerConfig;

    private final Scheduler scheduler;

    private final RegionControlService regionControlService;

    private BluePulsarListener<EmptyEvent> pulsarListener;

    public RegionInfosInvalidConsumer(BlueConsumerConfig blueConsumerConfig, Scheduler scheduler, RegionControlService regionControlService) {
        this.blueConsumerConfig = blueConsumerConfig;
        this.scheduler = scheduler;
        this.regionControlService = regionControlService;
    }

    @PostConstruct
    private void init() {
        Consumer<EmptyEvent> dataConsumer = emptyEvent ->
                ofNullable(emptyEvent)
                        .ifPresent(ee -> just(ee).publishOn(scheduler)
                                .then(regionControlService.invalidAllCache())
                                .doOnError(throwable -> LOGGER.info("regionControlService.invalidAllCache() failed, ee = {}, throwable = {}", ee, throwable))
                                .subscribe(ig -> LOGGER.info("regionControlService.invalidAllCache(), ig = {}, ee = {}", ig, ee)));

        this.pulsarListener = generateListener(blueConsumerConfig.getByKey(REGION_INFOS_INVALID.name), dataConsumer);
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
