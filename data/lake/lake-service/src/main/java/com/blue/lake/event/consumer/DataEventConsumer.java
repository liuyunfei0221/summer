package com.blue.lake.event.consumer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.event.DataEvent;
import com.blue.basic.model.exps.BlueException;
import com.blue.lake.config.blue.BlueConsumerConfig;
import com.blue.lake.service.inter.LakeService;
import com.blue.pulsar.component.BluePulsarBatchListener;
import org.apache.pulsar.client.api.PulsarClient;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.REQUEST_EVENT;
import static com.blue.basic.constant.common.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.pulsar.api.generator.BluePulsarListenerGenerator.generateBatchListener;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;
import static reactor.core.publisher.Mono.*;

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

    private final LakeService lakeService;

    private BluePulsarBatchListener<DataEvent> pulsarBatchListener;

    public DataEventConsumer(PulsarClient pulsarClient, BlueConsumerConfig blueConsumerConfig, LakeService lakeService) {
        this.pulsarClient = pulsarClient;
        this.blueConsumerConfig = blueConsumerConfig;
        this.lakeService = lakeService;
    }

    @PostConstruct
    private void init() {
        Consumer<List<DataEvent>> dataConsumer = dataEvents ->
                ofNullable(dataEvents)
                        .ifPresent(des -> just(des).flatMap(lakeService::handleEvents)
                                .switchIfEmpty(defer(() -> error(() -> new BlueException(INTERNAL_SERVER_ERROR))))
                                .doOnError(t -> LOGGER.info("handleEvents(List<DataEvent> dataEvents) failed, des = {}, t = {}", des, t))
                                .subscribe(b -> LOGGER.info("handleEvents(List<DataEvent> dataEvents), b = {}, des = {}", b, des)));

        this.pulsarBatchListener = generateBatchListener(pulsarClient, blueConsumerConfig.getByKey(REQUEST_EVENT.name), dataConsumer, DataEvent.class);
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
        this.pulsarBatchListener.run();
        LOGGER.warn("pulsarBatchListener start...");
    }

    @Override
    public void stop() {
        this.pulsarBatchListener.shutdown();
        LOGGER.warn("pulsarBatchListener shutdown...");
    }

}
