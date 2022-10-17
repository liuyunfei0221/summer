package com.blue.shine.event.consumer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.event.IdentityEvent;
import com.blue.basic.model.exps.BlueException;
import com.blue.pulsar.component.BluePulsarListener;
import com.blue.shine.config.blue.BlueConsumerConfig;
import com.blue.shine.service.inter.ShineService;
import org.apache.pulsar.client.api.PulsarClient;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.SHINE_DELETE;
import static com.blue.basic.constant.common.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.pulsar.api.generator.BluePulsarListenerGenerator.generateListener;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;


/**
 * shine delete consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public final class ShineDeleteConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(ShineDeleteConsumer.class);

    private final PulsarClient pulsarClient;

    private final BlueConsumerConfig blueConsumerConfig;

    private final Scheduler scheduler;

    private final ShineService shineService;

    private BluePulsarListener<IdentityEvent> pulsarListener;

    public ShineDeleteConsumer(PulsarClient pulsarClient, BlueConsumerConfig blueConsumerConfig, Scheduler scheduler, ShineService shineService) {
        this.pulsarClient = pulsarClient;
        this.blueConsumerConfig = blueConsumerConfig;
        this.scheduler = scheduler;
        this.shineService = shineService;
    }

    @PostConstruct
    private void init() {
        Consumer<IdentityEvent> dataConsumer = ie ->
                ofNullable(ie)
                        .ifPresent(id -> just(id).publishOn(scheduler).flatMap(shineService::deleteShineEvent)
                                .switchIfEmpty(defer(() -> error(() -> new BlueException(INTERNAL_SERVER_ERROR))))
                                .doOnError(throwable -> LOGGER.info("shineService.deleteShineEvent(IdentityEvent identityEvent) failed, ie = {}, throwable = {}", ie, throwable))
                                .subscribe(b -> LOGGER.info("shineService.deleteShineEvent(IdentityEvent identityEvent), b = {}, ie = {}", b, ie)));

        this.pulsarListener = generateListener(pulsarClient, blueConsumerConfig.getByKey(SHINE_DELETE.name), dataConsumer);
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
