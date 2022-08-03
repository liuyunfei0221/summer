package com.blue.auth.event.consumer;

import com.blue.auth.component.access.AccessInfoCache;
import com.blue.auth.config.blue.BlueConsumerConfig;
import com.blue.auth.event.model.InvalidLocalAccessEvent;
import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.exps.BlueException;
import com.blue.pulsar.api.generator.BluePulsarListenerGenerator;
import com.blue.pulsar.common.BluePulsarListener;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.INVALID_LOCAL_ACCESS;
import static com.blue.basic.constant.common.ResponseElement.INTERNAL_SERVER_ERROR;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.*;
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

    private final Scheduler scheduler;

    private final AccessInfoCache accessInfoCache;

    private BluePulsarListener<InvalidLocalAccessEvent> invalidLocalAccessConsumer;

    public InvalidLocalAccessConsumer(BlueConsumerConfig blueConsumerConfig, Scheduler scheduler, AccessInfoCache accessInfoCache) {
        this.blueConsumerConfig = blueConsumerConfig;
        this.scheduler = scheduler;
        this.accessInfoCache = accessInfoCache;
    }

    @PostConstruct
    private void init() {
        Consumer<InvalidLocalAccessEvent> invalidLocalAccessDataConsumer = invalidLocalAccessEvent ->
                ofNullable(invalidLocalAccessEvent)
                        .map(InvalidLocalAccessEvent::getKeyId)
                        .ifPresent(kid -> just(kid).publishOn(scheduler).map(accessInfoCache::invalidLocalAccessInfo)
                                .switchIfEmpty(defer(() -> error(() -> new BlueException(INTERNAL_SERVER_ERROR))))
                                .doOnError(throwable -> LOGGER.info("accessInfoCache.invalidLocalAccessInfo(kid) failed, kid = {}, throwable = {}", kid, throwable))
                                .subscribe(b -> LOGGER.info("accessInfoCache.invalidLocalAccessInfo(kid), b = {}, kid = {}", b, kid)));

        this.invalidLocalAccessConsumer = BluePulsarListenerGenerator.generateListener(blueConsumerConfig.getByKey(INVALID_LOCAL_ACCESS.name), invalidLocalAccessDataConsumer);
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
