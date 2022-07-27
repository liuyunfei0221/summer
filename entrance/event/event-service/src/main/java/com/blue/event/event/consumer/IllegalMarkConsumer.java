package com.blue.event.event.consumer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.common.IllegalMarkEvent;
import com.blue.event.component.illegal.IllegalAsserter;
import com.blue.event.config.blue.BlueConsumerConfig;
import com.blue.pulsar.api.generator.BluePulsarListenerGenerator;
import com.blue.pulsar.common.BluePulsarListener;
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

    private final IllegalAsserter illegalAsserter;

    private final Scheduler scheduler;

    private final BlueConsumerConfig blueConsumerConfig;

    private BluePulsarListener<IllegalMarkEvent> illegalMarkEventConsumer;

    public IllegalMarkConsumer(IllegalAsserter illegalAsserter, Scheduler scheduler, BlueConsumerConfig blueConsumerConfig) {
        this.illegalAsserter = illegalAsserter;
        this.scheduler = scheduler;
        this.blueConsumerConfig = blueConsumerConfig;
    }

    @PostConstruct
    private void init() {
        Consumer<IllegalMarkEvent> illegalMarkEventDataConsumer = illegalMarkEvent ->
                just(illegalMarkEvent)
                        .publishOn(scheduler)
                        .flatMap(illegalAsserter::handleIllegalMarkEvent)
                        .doOnError(t -> LOGGER.error("mark jwt or ip -> FAILED,illegalMarkEvent = {}, t = {}", illegalMarkEvent, t))
                        .subscribe(b -> LOGGER.warn("mark jwt or ip -> SUCCESS, illegalMarkEvent = {}, b = {}", illegalMarkEvent, b));

        this.illegalMarkEventConsumer = BluePulsarListenerGenerator.generateListener(blueConsumerConfig.getByKey(ILLEGAL_MARK.name), illegalMarkEventDataConsumer);
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
        this.illegalMarkEventConsumer.run();
        LOGGER.warn("invalidClusterLocalAuthConsumer start...");
    }

    @Override
    public void stop() {
        this.illegalMarkEventConsumer.shutdown();
        LOGGER.warn("invalidClusterLocalAuthConsumer shutdown...");
    }

}
