package com.blue.lake.event.consumer;

import com.blue.base.component.lifecycle.inter.BlueLifecycle;
import com.blue.base.model.base.DataEvent;
import com.blue.lake.config.blue.BlueConsumerConfig;
import com.blue.lake.service.inter.LakeService;
import com.blue.pulsar.common.BluePulsarConsumer;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.base.constant.base.BlueTopic.REQUEST_EVENT;
import static com.blue.pulsar.api.generator.BluePulsarConsumerGenerator.generateConsumer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * data event consumer
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused"})
public final class DataEventConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(DataEventConsumer.class);

    private final LakeService lakeService;

    private final BlueConsumerConfig blueConsumerConfig;

    private BluePulsarConsumer<DataEvent> dataEventConsumer;

    public DataEventConsumer(LakeService lakeService, BlueConsumerConfig blueConsumerConfig) {
        this.lakeService = lakeService;
        this.blueConsumerConfig = blueConsumerConfig;
    }

    @PostConstruct
    private void init() {
        Consumer<DataEvent> dataEventDataConsumer = dataEvent ->
                ofNullable(dataEvent)
                        .ifPresent(lakeService::insertEvent);

        this.dataEventConsumer = generateConsumer(blueConsumerConfig.getByKey(REQUEST_EVENT.name), dataEventDataConsumer);
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
        this.dataEventConsumer.run();
        LOGGER.warn("dataEventConsumer start...");
    }

    @Override
    public void stop() {
        this.dataEventConsumer.shutdown();
        LOGGER.warn("dataEventConsumer shutdown...");
    }

}
