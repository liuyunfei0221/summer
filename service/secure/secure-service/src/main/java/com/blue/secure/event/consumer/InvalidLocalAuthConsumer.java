package com.blue.secure.event.consumer;

import com.blue.base.component.lifecycle.inter.BlueLifecycle;
import com.blue.pulsar.common.BluePulsarConsumer;
import com.blue.secure.api.model.InvalidLocalAuthParam;
import com.blue.secure.config.blue.BlueConsumerConfig;
import com.blue.secure.service.inter.SecureService;
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

    private final SecureService secureService;

    private final BlueConsumerConfig blueConsumerConfig;

    private BluePulsarConsumer<InvalidLocalAuthParam> invalidClusterLocalAuthConsumer;

    public InvalidLocalAuthConsumer(SecureService secureService, BlueConsumerConfig blueConsumerConfig) {
        this.secureService = secureService;
        this.blueConsumerConfig = blueConsumerConfig;
    }

    @PostConstruct
    private void init() {
        Consumer<InvalidLocalAuthParam> invalidClusterLocalAuthDataConsumer = invalidLocalAuthParam ->
                ofNullable(invalidLocalAuthParam)
                        .ifPresent(ilap -> {
                            LOGGER.info("invalidClusterLocalAuthDataConsumer received, ilap = {}", ilap);
                            ofNullable(ilap.getKeyId())
                                    .ifPresent(keyId -> secureService.invalidLocalAuthByKeyId(keyId)
                                            .subscribe(b ->
                                                    LOGGER.info("secureService.invalidLocalAuthByKeyId(keyId), b = {}, ilap = {}", b, ilap)
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
