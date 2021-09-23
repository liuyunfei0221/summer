package com.blue.data.config.mq.consumer;

import com.blue.base.component.lifecycle.inter.BlueLifecycle;
import com.blue.base.constant.base.BlueDataAttrKey;
import com.blue.base.model.event.data.DataEvent;
import com.blue.data.common.statistics.StatisticsProcessor;
import com.blue.data.config.blue.BlueConsumerConfig;
import com.blue.jwt.common.JwtProcessor;
import com.blue.pulsar.api.conf.ConsumerConfParams;
import com.blue.pulsar.common.BluePulsarConsumer;
import com.blue.secure.api.model.MemberPayload;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.function.Consumer;

import static com.blue.base.constant.base.BlueTopic.REQUEST_EVENT;
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

    private final StatisticsProcessor statisticsProcessor;

    private final BlueConsumerConfig blueConsumerConfig;

    private BluePulsarConsumer<DataEvent> dataEventConsumer;

    public DataEventConsumer(JwtProcessor<MemberPayload> jwtProcessor, StatisticsProcessor statisticsProcessor, BlueConsumerConfig blueConsumerConfig) {
        this.jwtProcessor = jwtProcessor;
        this.statisticsProcessor = statisticsProcessor;
        this.blueConsumerConfig = blueConsumerConfig;
    }

    @PostConstruct
    private void init() {
        Consumer<DataEvent> dataEventDataConsumer = dataEvent ->
                ofNullable(dataEvent)
                        .ifPresent(de -> {
                            //TODO 行为分析
                            String accessJson = de.getData(BlueDataAttrKey.ACCESS.key);
                            String jwtStr = de.getData(BlueDataAttrKey.JWT.key);

                            LOGGER.warn("de = {}", de);

                            Map<String, String> entries = de.getEntries();
                            if (jwtStr != null && !"".equals(jwtStr))
                                try {
                                    MemberPayload memberPayload = jwtProcessor.parse(jwtStr);
                                    entries.put("memberId", memberPayload.getId());
                                } catch (Exception e) {
                                    LOGGER.error("jwtStr解析失败, jwtStr = {}, e = {}", jwtStr, e);
                                }

                            statisticsProcessor.process(entries);
                        });

        ConsumerConfParams dataEventDeploy = blueConsumerConfig.getByKey(REQUEST_EVENT.name);
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
