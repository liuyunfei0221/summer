package com.blue.secure.event.consumer;

import com.blue.base.component.lifecycle.inter.BlueLifecycle;
import com.blue.base.model.base.KeyExpireParam;
import com.blue.pulsar.common.BluePulsarConsumer;
import com.blue.secure.component.auth.AuthBatchExpireProcessor;
import com.blue.secure.config.blue.BlueConsumerConfig;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.base.constant.base.BlueTopic.AUTH_EXPIRE;
import static com.blue.pulsar.api.generator.BluePulsarConsumerGenerator.generateConsumer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * auth expire consumer
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class AuthExpireConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(AuthExpireConsumer.class);

    private final BlueConsumerConfig blueConsumerConfig;

    private final AuthBatchExpireProcessor authBatchExpireProcessor;

    private BluePulsarConsumer<KeyExpireParam> authExpireConsumer;

    public AuthExpireConsumer(BlueConsumerConfig blueConsumerConfig, AuthBatchExpireProcessor authBatchExpireProcessor) {
        this.blueConsumerConfig = blueConsumerConfig;
        this.authBatchExpireProcessor = authBatchExpireProcessor;
    }

    @PostConstruct
    private void init() {
        Consumer<KeyExpireParam> authExpireDataConsumer = keyExpireParam ->
                ofNullable(keyExpireParam)
                        .ifPresent(kep -> {
                            LOGGER.info("authExpireDataConsumer received, kep = {}", kep);
                            authBatchExpireProcessor.expireKey(kep);
                        });

        this.authExpireConsumer = generateConsumer(blueConsumerConfig.getByKey(AUTH_EXPIRE.name), authExpireDataConsumer);
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
        this.authExpireConsumer.run();
        LOGGER.warn("authExpireConsumer start...");
    }

    @Override
    public void stop() {
        this.authExpireConsumer.shutdown();
        LOGGER.warn("authExpireConsumer shutdown...");
    }

}
