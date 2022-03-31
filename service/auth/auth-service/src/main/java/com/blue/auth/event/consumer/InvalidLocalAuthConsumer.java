package com.blue.auth.event.consumer;

import com.blue.auth.config.blue.BlueConsumerConfig;
import com.blue.auth.service.inter.AuthService;
import com.blue.base.component.lifecycle.inter.BlueLifecycle;
import com.blue.pulsar.common.BluePulsarConsumer;
import com.blue.auth.api.model.InvalidLocalAuthParam;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.base.constant.base.BlueTopic.INVALID_LOCAL_AUTH;
import static com.blue.pulsar.api.generator.BluePulsarConsumerGenerator.generateConsumer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;


/**
 * invalid auth from local cache consumer
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public final class InvalidLocalAuthConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(InvalidLocalAuthConsumer.class);

    private final AuthService authService;

    private final BlueConsumerConfig blueConsumerConfig;

    private BluePulsarConsumer<InvalidLocalAuthParam> invalidClusterLocalAuthConsumer;

    public InvalidLocalAuthConsumer(AuthService authService, BlueConsumerConfig blueConsumerConfig) {
        this.authService = authService;
        this.blueConsumerConfig = blueConsumerConfig;
    }

    @PostConstruct
    private void init() {
        Consumer<InvalidLocalAuthParam> invalidClusterLocalAuthDataConsumer = invalidLocalAuthParam ->
                ofNullable(invalidLocalAuthParam)
                        .ifPresent(ilap -> {
                            LOGGER.info("invalidClusterLocalAuthDataConsumer received, ilap = {}", ilap);
                            ofNullable(ilap.getKeyId())
                                    .ifPresent(keyId -> authService.invalidLocalAuthByKeyId(keyId)
                                            .subscribe(b ->
                                                    LOGGER.info("authService.invalidLocalAuthByKeyId(keyId), b = {}, ilap = {}", b, ilap)
                                            )
                                    );
                        });

        this.invalidClusterLocalAuthConsumer = generateConsumer(blueConsumerConfig.getByKey(INVALID_LOCAL_AUTH.name), invalidClusterLocalAuthDataConsumer);
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
        this.invalidClusterLocalAuthConsumer.run();
        LOGGER.warn("invalidClusterLocalAuthConsumer start...");
    }

    @Override
    public void stop() {
        this.invalidClusterLocalAuthConsumer.shutdown();
        LOGGER.warn("invalidClusterLocalAuthConsumer shutdown...");
    }

}