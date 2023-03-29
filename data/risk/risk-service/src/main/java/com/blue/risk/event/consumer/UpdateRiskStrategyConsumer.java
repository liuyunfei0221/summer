package com.blue.risk.event.consumer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.exps.BlueException;
import com.blue.pulsar.api.generator.BluePulsarListenerGenerator;
import com.blue.pulsar.component.BluePulsarListener;
import com.blue.risk.api.model.RiskStrategyInfo;
import com.blue.risk.component.risk.RiskProcessor;
import com.blue.risk.config.blue.BlueConsumerConfig;
import org.apache.pulsar.client.api.PulsarClient;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.RISK_STRATEGY_UPDATE;
import static com.blue.basic.constant.common.ResponseElement.INTERNAL_SERVER_ERROR;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;
import static reactor.core.publisher.Mono.*;

/**
 * update risk strategy consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public final class UpdateRiskStrategyConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(UpdateRiskStrategyConsumer.class);

    private final PulsarClient pulsarClient;

    private final BlueConsumerConfig blueConsumerConfig;

    private final RiskProcessor riskProcessor;

    private BluePulsarListener<RiskStrategyInfo> pulsarListener;

    public UpdateRiskStrategyConsumer(PulsarClient pulsarClient, BlueConsumerConfig blueConsumerConfig, RiskProcessor riskProcessor) {
        this.pulsarClient = pulsarClient;
        this.blueConsumerConfig = blueConsumerConfig;
        this.riskProcessor = riskProcessor;
    }

    @PostConstruct
    private void init() {
        Consumer<RiskStrategyInfo> dataConsumer = riskStrategyInfo ->
                ofNullable(riskStrategyInfo)
                        .ifPresent(rsi -> just(rsi).flatMap(riskProcessor::updateStrategy)
                                .switchIfEmpty(defer(() -> error(() -> new BlueException(INTERNAL_SERVER_ERROR))))
                                .doOnError(t -> LOGGER.error("updateCurrentStrategy failed, de = {}, t = {}", rsi, t))
                                .subscribe(b -> LOGGER.info("updateCurrentStrategy, b = {}, de = {}", b, rsi)));

        this.pulsarListener = BluePulsarListenerGenerator.generateListener(pulsarClient, blueConsumerConfig.getByKey(RISK_STRATEGY_UPDATE.name), dataConsumer);
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
