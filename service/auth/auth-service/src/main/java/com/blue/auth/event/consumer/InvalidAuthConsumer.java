package com.blue.auth.event.consumer;

import com.blue.auth.config.blue.BlueConsumerConfig;
import com.blue.auth.service.inter.AuthControlService;
import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.common.InvalidAuthEvent;
import com.blue.pulsar.api.generator.BluePulsarListenerGenerator;
import com.blue.pulsar.common.BluePulsarListener;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.INVALID_AUTH;
import static com.blue.pulsar.api.generator.BluePulsarListenerGenerator.generateListener;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
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

    private final AuthControlService authControlService;

    private BluePulsarListener<InvalidAuthEvent> invalidAuthConsumer;

    public InvalidAuthConsumer(BlueConsumerConfig blueConsumerConfig, AuthControlService authControlService) {
        this.blueConsumerConfig = blueConsumerConfig;
        this.authControlService = authControlService;
    }

    @PostConstruct
    private void init() {
        Consumer<InvalidAuthEvent> invalidAuthDataConsumer = invalidLocalAuthEvent ->
                ofNullable(invalidLocalAuthEvent)
                        .map(InvalidAuthEvent::getMemberId)
                        .ifPresent(memberId -> authControlService.invalidateAuthByMemberId(memberId)
                                .doOnError(throwable -> LOGGER.info("controlService.invalidateAuthByMemberId(memberId) failed, memberId = {}, throwable = {}", memberId, throwable))
                                .subscribe(b -> LOGGER.info("controlService.invalidateAuthByMemberId(memberId), b = {}, memberId = {}", b, memberId)));

        this.invalidAuthConsumer = BluePulsarListenerGenerator.generateListener(blueConsumerConfig.getByKey(INVALID_AUTH.name), invalidAuthDataConsumer);
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
        this.invalidAuthConsumer.run();
        LOGGER.warn("invalidAuthConsumer start...");
    }

    @Override
    public void stop() {
        this.invalidAuthConsumer.shutdown();
        LOGGER.warn("invalidAuthConsumer shutdown...");
    }

}
