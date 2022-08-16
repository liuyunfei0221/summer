package com.blue.auth.event.consumer;

import com.blue.auth.config.blue.BlueConsumerConfig;
import com.blue.auth.service.inter.AuthControlService;
import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.event.InvalidAuthEvent;
import com.blue.basic.model.exps.BlueException;
import com.blue.pulsar.component.BluePulsarListener;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.INVALID_AUTH;
import static com.blue.basic.constant.common.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.pulsar.api.generator.BluePulsarListenerGenerator.generateListener;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;


/**
 * invalid global auth consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public final class InvalidAuthConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(InvalidAuthConsumer.class);

    private final BlueConsumerConfig blueConsumerConfig;

    private final Scheduler scheduler;

    private final AuthControlService authControlService;

    private BluePulsarListener<InvalidAuthEvent> pulsarListener;

    public InvalidAuthConsumer(BlueConsumerConfig blueConsumerConfig, Scheduler scheduler, AuthControlService authControlService) {
        this.blueConsumerConfig = blueConsumerConfig;
        this.scheduler = scheduler;
        this.authControlService = authControlService;
    }

    @PostConstruct
    private void init() {
        Consumer<InvalidAuthEvent> dataConsumer = invalidLocalAuthEvent ->
                ofNullable(invalidLocalAuthEvent)
                        .map(InvalidAuthEvent::getMemberId)
                        .ifPresent(mid -> just(mid).publishOn(scheduler).map(authControlService::invalidateAuthByMemberId)
                                .switchIfEmpty(defer(() -> error(() -> new BlueException(INTERNAL_SERVER_ERROR))))
                                .doOnError(throwable -> LOGGER.info("authControlService.invalidateAuthByMemberId(mid) failed, mid = {}, throwable = {}", mid, throwable))
                                .subscribe(b -> LOGGER.info("authControlService.invalidateAuthByMemberId(mid), b = {}, mid = {}", b, mid)));

        this.pulsarListener = generateListener(blueConsumerConfig.getByKey(INVALID_AUTH.name), dataConsumer);
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
