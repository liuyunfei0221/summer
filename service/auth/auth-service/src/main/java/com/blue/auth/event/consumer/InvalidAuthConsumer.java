package com.blue.auth.event.consumer;

import com.blue.auth.config.blue.BlueConsumerConfig;
import com.blue.auth.service.inter.AuthControlService;
import com.blue.base.component.lifecycle.inter.BlueLifecycle;
import com.blue.base.model.common.InvalidAuthEvent;
import com.blue.pulsar.common.BluePulsarConsumer;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.base.constant.common.BlueTopic.INVALID_AUTH;
import static com.blue.pulsar.api.generator.BluePulsarConsumerGenerator.generateConsumer;
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

    private final AuthControlService authControlService;

    private final BlueConsumerConfig blueConsumerConfig;

    private BluePulsarConsumer<InvalidAuthEvent> invalidAuthConsumer;

    public InvalidAuthConsumer(AuthControlService authControlService, BlueConsumerConfig blueConsumerConfig) {
        this.authControlService = authControlService;
        this.blueConsumerConfig = blueConsumerConfig;
    }

    @PostConstruct
    private void init() {
        Consumer<InvalidAuthEvent> invalidAuthDataConsumer = invalidLocalAuthEvent ->
                ofNullable(invalidLocalAuthEvent)
                        .map(InvalidAuthEvent::getMemberId)
                        .ifPresent(memberId -> authControlService.invalidateAuthByMemberId(memberId)
                                .doOnError(throwable -> LOGGER.info("controlService.invalidateAuthByMemberId(memberId) failed, memberId = {}, throwable = {}", memberId, throwable))
                                .subscribe(b -> LOGGER.info("controlService.invalidateAuthByMemberId(memberId), b = {}, memberId = {}", b, memberId)));

        this.invalidAuthConsumer = generateConsumer(blueConsumerConfig.getByKey(INVALID_AUTH.name), invalidAuthDataConsumer);
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
