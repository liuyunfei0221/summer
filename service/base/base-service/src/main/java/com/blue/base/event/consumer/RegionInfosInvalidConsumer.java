package com.blue.base.event.consumer;

import com.blue.base.component.lifecycle.inter.BlueLifecycle;
import com.blue.base.config.blue.BlueConsumerConfig;
import com.blue.base.model.common.EmptyEvent;
import com.blue.base.service.inter.RegionControlService;
import com.blue.pulsar.common.BluePulsarConsumer;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.base.constant.common.BlueTopic.REGION_INFOS_INVALID;
import static com.blue.pulsar.api.generator.BluePulsarConsumerGenerator.generateConsumer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * invalid region info consumer
 *
 * @author liuyunfei
 */
public final class RegionInfosInvalidConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(RegionInfosInvalidConsumer.class);

    private final BlueConsumerConfig blueConsumerConfig;

    private final RegionControlService regionControlService;

    private BluePulsarConsumer<EmptyEvent> regionInfosInvalidConsumer;

    public RegionInfosInvalidConsumer(BlueConsumerConfig blueConsumerConfig, RegionControlService regionControlService) {
        this.blueConsumerConfig = blueConsumerConfig;
        this.regionControlService = regionControlService;
    }

    @PostConstruct
    private void init() {
        Consumer<EmptyEvent> regionInfosInvalidConsumerDataConsumer = emptyEvent ->
                ofNullable(emptyEvent)
                        .ifPresent(ee -> {
                            LOGGER.info("regionInfosInvalidConsumerDataConsumer received");
                            regionControlService.invalidAllCache()
                                    .doOnError(throwable -> LOGGER.info("controlService.invalidAllCache() failed, throwable = {}", throwable))
                                    .subscribe(v -> LOGGER.info("controlService.invalidAllCache()"));
                        });

        this.regionInfosInvalidConsumer = generateConsumer(blueConsumerConfig.getByKey(REGION_INFOS_INVALID.name), regionInfosInvalidConsumerDataConsumer);
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
        this.regionInfosInvalidConsumer.run();
        LOGGER.warn("regionInfosInvalidConsumer start...");
    }

    @Override
    public void stop() {
        this.regionInfosInvalidConsumer.shutdown();
        LOGGER.warn("regionInfosInvalidConsumer shutdown...");
    }

}
