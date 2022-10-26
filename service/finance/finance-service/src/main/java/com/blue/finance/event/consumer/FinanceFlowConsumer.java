package com.blue.finance.event.consumer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.exps.BlueException;
import com.blue.finance.config.blue.BlueConsumerConfig;
import com.blue.finance.repository.entity.FinanceFlow;
import com.blue.finance.service.inter.FinanceFlowService;
import com.blue.pulsar.component.BluePulsarListener;
import org.apache.pulsar.client.api.PulsarClient;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.FINANCE_FLOW;
import static com.blue.basic.constant.common.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.pulsar.api.generator.BluePulsarListenerGenerator.generateListener;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;


/**
 * finance flow consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public final class FinanceFlowConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(FinanceFlowConsumer.class);

    private final PulsarClient pulsarClient;

    private final BlueConsumerConfig blueConsumerConfig;

    private final FinanceFlowService financeFlowService;

    private BluePulsarListener<FinanceFlow> pulsarListener;

    public FinanceFlowConsumer(PulsarClient pulsarClient, BlueConsumerConfig blueConsumerConfig, FinanceFlowService financeFlowService) {
        this.pulsarClient = pulsarClient;
        this.blueConsumerConfig = blueConsumerConfig;
        this.financeFlowService = financeFlowService;
    }

    @PostConstruct
    private void init() {
        Consumer<FinanceFlow> dataConsumer = financeFlow ->
                ofNullable(financeFlow)
                        .ifPresent(ff -> just(ff).flatMap(financeFlowService::insertFinanceFlow)
                                .switchIfEmpty(defer(() -> error(() -> new BlueException(INTERNAL_SERVER_ERROR))))
                                .doOnError(throwable -> LOGGER.info("financeFlowService.insertFinanceFlow(ff) failed, ff = {}, throwable = {}", ff, throwable))
                                .subscribe(b -> LOGGER.info("financeFlowService.insertFinanceFlow(ff), b = {}, ff = {}", b, ff)));

        this.pulsarListener = generateListener(pulsarClient, blueConsumerConfig.getByKey(FINANCE_FLOW.name), dataConsumer);
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
