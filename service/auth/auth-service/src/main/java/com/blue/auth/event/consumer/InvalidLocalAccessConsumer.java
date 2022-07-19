package com.blue.auth.event.consumer;

import com.blue.auth.component.access.AccessInfoCache;
import com.blue.auth.config.blue.BlueConsumerConfig;
import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.common.InvalidLocalAccessEvent;
import com.blue.pulsar.common.BluePulsarConsumer;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.INVALID_LOCAL_ACCESS;
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

    private final BlueConsumerConfig blueConsumerConfig;

    private final AccessInfoCache accessInfoCache;

    private BluePulsarConsumer<InvalidLocalAccessEvent> invalidLocalAccessConsumer;

    public InvalidLocalAccessConsumer(BlueConsumerConfig blueConsumerConfig, AccessInfoCache accessInfoCache) {
        this.blueConsumerConfig = blueConsumerConfig;
        this.accessInfoCache = accessInfoCache;
    }

    @PostConstruct
    private void init() {
        Consumer<InvalidLocalAccessEvent> invalidLocalAccessDataConsumer = invalidLocalAccessEvent ->
                ofNullable(invalidLocalAccessEvent)
                        .map(InvalidLocalAccessEvent::getKeyId)
                        .ifPresent(keyId -> accessInfoCache.invalidLocalAccessInfo(keyId)
                                .doOnError(throwable -> LOGGER.info("accessInfoCache.invalidLocalAccessInfo(keyId) failed, keyId = {}, throwable = {}", keyId, throwable))
                                .subscribe(b -> LOGGER.info("accessInfoCache.invalidLocalAccessInfo(keyId), b = {}, keyId = {}", b, keyId)));

        this.invalidLocalAccessConsumer = generateConsumer(blueConsumerConfig.getByKey(INVALID_LOCAL_ACCESS.name), invalidLocalAccessDataConsumer);
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
        this.invalidLocalAccessConsumer.run();
        LOGGER.warn("invalidLocalAccessConsumer start...");
    }

    @Override
    public void stop() {
        this.invalidLocalAccessConsumer.shutdown();
        LOGGER.warn("invalidLocalAccessConsumer shutdown...");
    }

}
