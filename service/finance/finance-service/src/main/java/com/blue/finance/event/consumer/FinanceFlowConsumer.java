package com.blue.finance.event.consumer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.finance.config.blue.BlueConsumerConfig;
import com.blue.finance.repository.entity.FinanceFlow;
import com.blue.finance.service.inter.FinanceFlowService;
import com.blue.pulsar.api.generator.BluePulsarListenerGenerator;
import com.blue.pulsar.common.BluePulsarListener;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.FINANCE_FLOW;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;


/**
 * finance flow consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused"})
public final class FinanceFlowConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(FinanceFlowConsumer.class);

    private final BlueConsumerConfig blueConsumerConfig;

    private final FinanceFlowService financeFlowService;

    private BluePulsarListener<FinanceFlow> financeFlowConsumer;

    public FinanceFlowConsumer(BlueConsumerConfig blueConsumerConfig, FinanceFlowService financeFlowService) {
        this.blueConsumerConfig = blueConsumerConfig;
        this.financeFlowService = financeFlowService;
    }

    @PostConstruct
    private void init() {
        Consumer<FinanceFlow> financeFlowDataConsumer = financeFlow ->
                ofNullable(financeFlow)
                        .ifPresent(ff -> financeFlowService.insertFinanceFlow(ff)
                                .doOnError(throwable -> LOGGER.info("financeFlowService.insertFinanceFlow(ff) failed, ff = {}, throwable = {}", ff, throwable))
                                .subscribe(b -> LOGGER.info("financeFlowService.insertFinanceFlow(ff), b = {}, ff = {}", b, ff)));

        this.financeFlowConsumer = BluePulsarListenerGenerator.generateListener(blueConsumerConfig.getByKey(FINANCE_FLOW.name), financeFlowDataConsumer);
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
        this.financeFlowConsumer.run();
        LOGGER.warn("financeFlowConsumer start...");
    }

    @Override
    public void stop() {
        this.financeFlowConsumer.shutdown();
        LOGGER.warn("financeFlowConsumer shutdown...");
    }

}
