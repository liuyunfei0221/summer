package com.blue.auth.event.consumer;

import com.blue.auth.api.model.InvalidLocalAccessEvent;
import com.blue.auth.component.access.AccessInfoCache;
import com.blue.auth.config.blue.BlueConsumerConfig;
import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.exps.BlueException;
import com.blue.pulsar.component.BluePulsarListener;
import org.apache.pulsar.client.api.PulsarClient;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.INVALID_LOCAL_ACCESS;
import static com.blue.basic.constant.common.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.pulsar.api.generator.BluePulsarListenerGenerator.generateListener;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;
import static reactor.core.publisher.Mono.*;


/**
 * invalid auth from local cache consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public final class InvalidLocalAccessConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(InvalidLocalAccessConsumer.class);

    private final PulsarClient pulsarClient;

    private final BlueConsumerConfig blueConsumerConfig;

    private final AccessInfoCache accessInfoCache;

    private BluePulsarListener<InvalidLocalAccessEvent> pulsarListener;

    public InvalidLocalAccessConsumer(PulsarClient pulsarClient, BlueConsumerConfig blueConsumerConfig, AccessInfoCache accessInfoCache) {
        this.pulsarClient = pulsarClient;
        this.blueConsumerConfig = blueConsumerConfig;
        this.accessInfoCache = accessInfoCache;
    }

    @PostConstruct
    private void init() {
        Consumer<InvalidLocalAccessEvent> dataConsumer = invalidLocalAccessEvent ->
                ofNullable(invalidLocalAccessEvent)
                        .map(InvalidLocalAccessEvent::getKeyId)
                        .ifPresent(kid -> just(kid).flatMap(accessInfoCache::invalidLocalAccessInfo)
                                .switchIfEmpty(defer(() -> error(() -> new BlueException(INTERNAL_SERVER_ERROR))))
                                .doOnError(throwable -> LOGGER.info("invalidLocalAccessInfo failed, kid = {}, throwable = {}", kid, throwable))
                                .subscribe(b -> LOGGER.info("invalidLocalAccessInfo, b = {}, kid = {}", b, kid)));

        this.pulsarListener = generateListener(pulsarClient, blueConsumerConfig.getByKey(INVALID_LOCAL_ACCESS.name), dataConsumer);
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
