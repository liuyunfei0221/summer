package com.blue.risk.config.mq.consumer;

import com.blue.base.component.lifecycle.inter.BlueLifecycle;
import com.blue.base.constant.base.BlueDataAttrKey;
import com.blue.base.constant.base.BlueTopic;
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
 * 消费端配置
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
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
                ofNullable(dataEvent)
                        .ifPresent(de -> {
                            //TODO 风控分析
                            String accessJson = de.getData(BlueDataAttrKey.ACCESS.key);
                            String jwtStr = de.getData(BlueDataAttrKey.JWT.key);

                            LOGGER.warn("de = {}", de);
                            LOGGER.warn("accessJson = {}", accessJson);
                            LOGGER.warn("jwtStr = {}", jwtStr);

                            if (jwtStr != null && !"".equals(jwtStr))
                                try {
                                    MemberPayload memberPayload = jwtProcessor.parse(jwtStr);
                                    LOGGER.warn("memberPayload = {}", memberPayload);
                                } catch (Exception e) {
                                    LOGGER.error("jwtStr解析失败, jwtStr = {}, e = {}", jwtStr, e);
                                }
                        });

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
