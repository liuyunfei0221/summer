package com.blue.auth.event.consumer;

import com.blue.auth.api.model.InvalidLocalAuthParam;
import com.blue.auth.component.access.AccessInfoCache;
import com.blue.auth.config.blue.BlueConsumerConfig;
import com.blue.base.component.lifecycle.inter.BlueLifecycle;
import com.blue.pulsar.common.BluePulsarConsumer;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.base.constant.base.BlueTopic.INVALID_LOCAL_ACCESS;
import static com.blue.pulsar.api.generator.BluePulsarConsumerGenerator.generateConsumer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;


/**
 * invalid auth from local cache consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public final class InvalidLocalAccessConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(InvalidLocalAccessConsumer.class);

    private final AccessInfoCache accessInfoCache;

    private final BlueConsumerConfig blueConsumerConfig;

    private BluePulsarConsumer<InvalidLocalAuthParam> invalidClusterLocalAuthConsumer;

    public InvalidLocalAccessConsumer(AccessInfoCache accessInfoCache, BlueConsumerConfig blueConsumerConfig) {
        this.accessInfoCache = accessInfoCache;
        this.blueConsumerConfig = blueConsumerConfig;
    }

    @PostConstruct
    private void init() {
        Consumer<InvalidLocalAuthParam> invalidClusterLocalAuthDataConsumer = invalidLocalAuthParam ->
                ofNullable(invalidLocalAuthParam)
                        .map(InvalidLocalAuthParam::getKeyId)
                        .ifPresent(keyId -> accessInfoCache.invalidLocalAccessInfo(keyId)
                                .subscribe(b ->
                                        LOGGER.info("authService.invalidLocalAuthByKeyId(keyId), b = {}, keyId = {}", b, keyId)
                                ));

        this.invalidClusterLocalAuthConsumer = generateConsumer(blueConsumerConfig.getByKey(INVALID_LOCAL_ACCESS.name), invalidClusterLocalAuthDataConsumer);
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
