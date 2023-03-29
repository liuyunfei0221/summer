package com.blue.base.event.consumer;

import com.blue.base.config.blue.BlueConsumerConfig;
import com.blue.base.service.inter.RegionControlService;
import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.event.EmptyEvent;
import com.blue.pulsar.component.BluePulsarListener;
import org.apache.pulsar.client.api.PulsarClient;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.REGION_INFOS_INVALID;
import static com.blue.pulsar.api.generator.BluePulsarListenerGenerator.generateListener;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;
import static reactor.core.publisher.Mono.just;

/**
 * invalid region info consumer
 *
 * @author liuyunfei
 */
public final class RegionInfosInvalidConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(RegionInfosInvalidConsumer.class);

    private final PulsarClient pulsarClient;

    private final BlueConsumerConfig blueConsumerConfig;

    private final RegionControlService regionControlService;

    private BluePulsarListener<EmptyEvent> pulsarListener;

    public RegionInfosInvalidConsumer(PulsarClient pulsarClient, BlueConsumerConfig blueConsumerConfig, RegionControlService regionControlService) {
        this.pulsarClient = pulsarClient;
        this.blueConsumerConfig = blueConsumerConfig;
        this.regionControlService = regionControlService;
    }

    @PostConstruct
    private void init() {
        Consumer<EmptyEvent> dataConsumer = emptyEvent ->
                ofNullable(emptyEvent)
                        .ifPresent(ee -> just(ee)
                                .then(regionControlService.invalidAllCache())
                                .doOnError(throwable -> LOGGER.info("invalidAllCache failed, ee = {}, throwable = {}", ee, throwable))
                                .subscribe(ig -> LOGGER.info("invalidAllCache, ig = {}, ee = {}", ig, ee)));

        this.pulsarListener = generateListener(pulsarClient, blueConsumerConfig.getByKey(REGION_INFOS_INVALID.name), dataConsumer);
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
