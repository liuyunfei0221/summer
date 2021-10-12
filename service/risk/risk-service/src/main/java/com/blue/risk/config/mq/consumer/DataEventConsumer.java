package com.blue.risk.config.mq.consumer;

import com.blue.base.common.auth.AuthProcessor;
import com.blue.base.component.lifecycle.inter.BlueLifecycle;
import com.blue.base.constant.base.BlueDataAttrKey;
import com.blue.base.constant.base.BlueTopic;
import com.blue.base.model.base.Access;
import com.blue.base.model.event.data.DataEvent;
import com.blue.jwt.common.JwtProcessor;
import com.blue.pulsar.api.conf.ConsumerConfParams;
import com.blue.pulsar.common.BluePulsarConsumer;
import com.blue.risk.config.blue.BlueConsumerConfig;
import com.blue.secure.api.model.MemberPayload;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

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

    private final JwtProcessor<MemberPayload> jwtProcessor;

    private final BlueConsumerConfig blueConsumerConfig;

    private BluePulsarConsumer<DataEvent> dataEventConsumer;

    public DataEventConsumer(JwtProcessor<MemberPayload> jwtProcessor, BlueConsumerConfig blueConsumerConfig) {
        this.jwtProcessor = jwtProcessor;
        this.blueConsumerConfig = blueConsumerConfig;
    }

    @PostConstruct
    private void init() {
        Consumer<DataEvent> dataEventDataConsumer = dataEvent ->
                //TODO 风控分析
                ofNullable(dataEvent)
                        .flatMap(de -> ofNullable(de.getData(BlueDataAttrKey.ACCESS.key))
                                .map(AuthProcessor::jsonToAccess)
                                .map(Access::getId)).ifPresent(access -> LOGGER.warn("access = {}", access));

        ConsumerConfParams dataEventDeploy = blueConsumerConfig.getByKey(BlueTopic.REQUEST_EVENT.name);
        this.dataEventConsumer = generateConsumer(dataEventDeploy, dataEventDataConsumer);
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
