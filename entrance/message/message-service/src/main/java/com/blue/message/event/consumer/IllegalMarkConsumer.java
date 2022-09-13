package com.blue.message.event.consumer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.event.IllegalMarkEvent;
import com.blue.message.component.illegal.IllegalAsserter;
import com.blue.message.config.blue.BlueConsumerConfig;
import com.blue.pulsar.component.BluePulsarListener;
import org.apache.pulsar.client.api.PulsarClient;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.ILLEGAL_MARK;
import static com.blue.pulsar.api.generator.BluePulsarListenerGenerator.generateListener;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * illegal mark consumer
 *
 * @author liuyunfei
 */
public final class IllegalMarkConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(IllegalMarkConsumer.class);

    private final PulsarClient pulsarClient;

    private final IllegalAsserter illegalAsserter;

    private final Scheduler scheduler;

    private final BlueConsumerConfig blueConsumerConfig;

    private BluePulsarListener<IllegalMarkEvent> pulsarListener;

    public IllegalMarkConsumer(PulsarClient pulsarClient, IllegalAsserter illegalAsserter, Scheduler scheduler, BlueConsumerConfig blueConsumerConfig) {
        this.pulsarClient = pulsarClient;
        this.illegalAsserter = illegalAsserter;
        this.scheduler = scheduler;
        this.blueConsumerConfig = blueConsumerConfig;
    }

    @PostConstruct
    private void init() {
        Consumer<IllegalMarkEvent> dataConsumer = illegalMarkEvent ->
                just(illegalMarkEvent)
                        .publishOn(scheduler)
                        .flatMap(illegalAsserter::handleIllegalMarkEvent)
                        .doOnError(t -> LOGGER.error("mark jwt or ip -> FAILED,illegalMarkEvent = {}, t = {}", illegalMarkEvent, t))
                        .subscribe(b ->
                                LOGGER.warn("mark jwt or ip -> SUCCESS, illegalMarkEvent = {}, b = {}", illegalMarkEvent, b));

        this.pulsarListener = generateListener(pulsarClient, blueConsumerConfig.getByKey(ILLEGAL_MARK.name), dataConsumer);
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
