package com.blue.analyze.event.consumer;

import com.blue.base.component.lifecycle.inter.BlueLifecycle;
import com.blue.base.model.base.DataEvent;
import com.blue.analyze.component.statistics.StatisticsProcessor;
import com.blue.analyze.config.blue.BlueConsumerConfig;
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

    private final StatisticsProcessor statisticsProcessor;

    private final BlueConsumerConfig blueConsumerConfig;

    private BluePulsarConsumer<DataEvent> dataEventConsumer;

    public DataEventConsumer(StatisticsProcessor statisticsProcessor, BlueConsumerConfig blueConsumerConfig) {
        this.statisticsProcessor = statisticsProcessor;
        this.blueConsumerConfig = blueConsumerConfig;
    }

    @PostConstruct
    private void init() {
        Consumer<DataEvent> dataEventDataConsumer = dataEvent ->
                ofNullable(dataEvent)
                        .ifPresent(de -> {
                            LOGGER.warn("de = {}", de);
                            statisticsProcessor.process(de.getEntries());
                        });

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
