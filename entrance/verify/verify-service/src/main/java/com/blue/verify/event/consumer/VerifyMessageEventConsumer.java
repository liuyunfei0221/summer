package com.blue.verify.event.consumer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.exps.BlueException;
import com.blue.pulsar.api.generator.BluePulsarListenerGenerator;
import com.blue.pulsar.component.BluePulsarListener;
import com.blue.verify.api.model.VerifyMessage;
import com.blue.verify.component.sender.VerifyMessageSenderProcessor;
import com.blue.verify.config.blue.BlueConsumerConfig;
import org.apache.pulsar.client.api.PulsarClient;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.VERIFY_MESSAGE;
import static com.blue.basic.constant.common.ResponseElement.INTERNAL_SERVER_ERROR;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * verify message event consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class VerifyMessageEventConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(VerifyMessageEventConsumer.class);

    private final PulsarClient pulsarClient;

    private final BlueConsumerConfig blueConsumerConfig;

    private final Scheduler scheduler;

    private final VerifyMessageSenderProcessor verifyMessageSenderProcessor;

    private BluePulsarListener<VerifyMessage> pulsarListener;

    public VerifyMessageEventConsumer(PulsarClient pulsarClient, BlueConsumerConfig blueConsumerConfig, Scheduler schedule,
                                      VerifyMessageSenderProcessor verifyMessageSenderProcessor) {
        this.pulsarClient = pulsarClient;
        this.blueConsumerConfig = blueConsumerConfig;
        this.scheduler = schedule;
        this.verifyMessageSenderProcessor = verifyMessageSenderProcessor;
    }

    @PostConstruct
    private void init() {
        Consumer<VerifyMessage> dataConsumer = verifyMessage ->
                ofNullable(verifyMessage)
                        .ifPresent(vm -> just(vm).publishOn(scheduler).map(verifyMessageSenderProcessor::send)
                                .switchIfEmpty(defer(() -> error(() -> new BlueException(INTERNAL_SERVER_ERROR))))
                                .doOnError(throwable -> LOGGER.info("verifyMessageSenderProcessor.send(VerifyMessage verifyMessage) failed, vm = {}, throwable = {}", vm, throwable))
                                .subscribe(b -> LOGGER.info("verifyMessageSenderProcessor.send(VerifyMessage verifyMessage), b = {}, vm = {}", b, vm)));

        this.pulsarListener = BluePulsarListenerGenerator.generateListener(pulsarClient, blueConsumerConfig.getByKey(VERIFY_MESSAGE.name), dataConsumer);
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
