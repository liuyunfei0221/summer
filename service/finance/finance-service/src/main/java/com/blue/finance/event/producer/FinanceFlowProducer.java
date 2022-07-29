package com.blue.finance.event.producer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.finance.config.blue.BlueProducerConfig;
import com.blue.finance.repository.entity.FinanceFlow;
import com.blue.pulsar.common.BluePulsarProducer;
import org.apache.pulsar.client.api.MessageId;
import reactor.util.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.FINANCE_FLOW;
import static com.blue.pulsar.api.generator.BluePulsarProducerGenerator.generateProducer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static reactor.util.Loggers.getLogger;

/**
 * finance flow producer
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public final class FinanceFlowProducer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(FinanceFlowProducer.class);

    private final ExecutorService executorService;

    private final BluePulsarProducer<FinanceFlow> financeFlowProducer;

    public FinanceFlowProducer(ExecutorService executorService, BlueProducerConfig blueProducerConfig) {
        this.executorService = executorService;
        this.financeFlowProducer = generateProducer(blueProducerConfig.getByKey(FINANCE_FLOW.name), FinanceFlow.class);
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
        LOGGER.warn("invalidLocalAccessProducer start...");
    }

    @Override
    public void stop() {
        this.financeFlowProducer.flush();
        this.financeFlowProducer.close();
        LOGGER.warn("financeFlowProducer shutdown...");
    }

    /**
     * send message
     *
     * @param financeFlow
     */
    public void send(FinanceFlow financeFlow) {
        CompletableFuture<MessageId> future = financeFlowProducer.sendAsync(financeFlow);
        LOGGER.info("financeFlowProducer send, financeFlow = {}", financeFlow);
        Consumer<MessageId> c = messageId ->
                LOGGER.info("financeFlowProducer send success, financeFlow = {}, messageId = {}", financeFlow, messageId);
        future.thenAcceptAsync(c, executorService);
    }

}
