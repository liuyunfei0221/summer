package com.blue.analyze.event.consumer;

import com.blue.analyze.component.statistics.StatisticsProcessor;
import com.blue.analyze.config.blue.BlueConsumerConfig;
import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.event.DataEvent;
import com.blue.basic.model.exps.BlueException;
import com.blue.pulsar.component.BluePulsarListener;
import org.apache.pulsar.client.api.PulsarClient;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.REQUEST_EVENT;
import static com.blue.basic.constant.common.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.pulsar.api.generator.BluePulsarListenerGenerator.generateListener;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * data event consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public final class DataEventConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(DataEventConsumer.class);

    private final PulsarClient pulsarClient;

    private final BlueConsumerConfig blueConsumerConfig;

    private final StatisticsProcessor statisticsProcessor;

    private BluePulsarListener<DataEvent> pulsarListener;

    public DataEventConsumer(PulsarClient pulsarClient, BlueConsumerConfig blueConsumerConfig, StatisticsProcessor statisticsProcessor) {
        this.pulsarClient = pulsarClient;
        this.blueConsumerConfig = blueConsumerConfig;
        this.statisticsProcessor = statisticsProcessor;
    }

    @PostConstruct
    private void init() {
        Consumer<DataEvent> dataConsumer = dataEvent ->
                ofNullable(dataEvent)
                        .ifPresent(de -> just(de).flatMap(statisticsProcessor::process)
                                .switchIfEmpty(defer(() -> error(() -> new BlueException(INTERNAL_SERVER_ERROR))))
                                .doOnError(t -> LOGGER.info("process failed, de = {}, t = {}", de, t))
                                .subscribe(b -> LOGGER.info("process, b = {}, de = {}", b, de)));

        this.pulsarListener = generateListener(pulsarClient, blueConsumerConfig.getByKey(REQUEST_EVENT.name), dataConsumer);
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
