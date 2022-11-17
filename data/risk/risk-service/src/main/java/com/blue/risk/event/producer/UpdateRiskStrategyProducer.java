package com.blue.risk.event.producer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.pulsar.component.BluePulsarProducer;
import com.blue.risk.api.model.RiskStrategyInfo;
import com.blue.risk.config.blue.BlueProducerConfig;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.PulsarClient;
import reactor.util.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.RISK_STRATEGY_UPDATE;
import static com.blue.pulsar.api.generator.BluePulsarProducerGenerator.generateProducer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static reactor.util.Loggers.getLogger;


/**
 * illegal mark producer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"Duplicates", "JavaDoc", "unused"})
public final class UpdateRiskStrategyProducer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(UpdateRiskStrategyProducer.class);

    private final ExecutorService executorService;

    private final BluePulsarProducer<RiskStrategyInfo> pulsarProducer;

    public UpdateRiskStrategyProducer(PulsarClient pulsarClient, ExecutorService executorService, BlueProducerConfig blueProducerConfig) {
        this.executorService = executorService;
        this.pulsarProducer = generateProducer(pulsarClient, blueProducerConfig.getByKey(RISK_STRATEGY_UPDATE.name), RiskStrategyInfo.class);
    }

    @Override
    public int startPrecedence() {
        return MIN_VALUE;
    }

    @Override
    public int stopPrecedence() {
        return MAX_VALUE;
    }

    @Override
    public void start() {
        LOGGER.warn("pulsarProducer start...");
    }

    @Override
    public void stop() {
        this.pulsarProducer.flush();
        this.pulsarProducer.close();
        LOGGER.warn("pulsarProducer shutdown...");
    }

    /**
     * send message
     *
     * @param riskStrategyInfo
     */
    public void send(RiskStrategyInfo riskStrategyInfo) {
        CompletableFuture<MessageId> future = pulsarProducer.sendAsync(riskStrategyInfo);
        LOGGER.info("pulsarProducer send, riskStrategyInfo = {}", riskStrategyInfo);
        Consumer<MessageId> c = messageId ->
                LOGGER.info("pulsarProducer send success, riskStrategyInfo = {}, messageId = {}", riskStrategyInfo, messageId);
        future.thenAcceptAsync(c, executorService);
    }

}
