package com.blue.auth.event.consumer;

import com.blue.auth.component.access.AccessBatchExpireProcessor;
import com.blue.auth.config.blue.BlueConsumerConfig;
import com.blue.base.component.lifecycle.inter.BlueLifecycle;
import com.blue.base.model.base.KeyExpireParam;
import com.blue.pulsar.common.BluePulsarConsumer;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.base.constant.base.BlueTopic.ACCESS_EXPIRE;
import static com.blue.pulsar.api.generator.BluePulsarConsumerGenerator.generateConsumer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * access expire consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class AccessExpireConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(AccessExpireConsumer.class);

    private final BlueConsumerConfig blueConsumerConfig;

    private final AccessBatchExpireProcessor accessBatchExpireProcessor;

    private BluePulsarConsumer<KeyExpireParam> authExpireConsumer;

    public AccessExpireConsumer(BlueConsumerConfig blueConsumerConfig, AccessBatchExpireProcessor accessBatchExpireProcessor) {
        this.blueConsumerConfig = blueConsumerConfig;
        this.accessBatchExpireProcessor = accessBatchExpireProcessor;
    }

    @PostConstruct
    private void init() {
        Consumer<KeyExpireParam> authExpireDataConsumer = keyExpireParam ->
                ofNullable(keyExpireParam)
                        .ifPresent(kep -> {
                            LOGGER.info("authExpireDataConsumer received, kep = {}", kep);
                            accessBatchExpireProcessor.expireKey(kep);
                            //noinspection UnusedAssignment
                            kep = null;
                        });

        this.authExpireConsumer = generateConsumer(blueConsumerConfig.getByKey(ACCESS_EXPIRE.name), authExpireDataConsumer);
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
