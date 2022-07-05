package com.blue.gateway.event.consumer;

import com.blue.base.component.lifecycle.inter.BlueLifecycle;
import com.blue.base.model.common.IllegalMarkEvent;
import com.blue.gateway.component.illegal.IllegalAsserter;
import com.blue.gateway.config.blue.BlueConsumerConfig;
import com.blue.pulsar.common.BluePulsarConsumer;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.base.constant.common.BlueTopic.ILLEGAL_MARK;
import static com.blue.pulsar.api.generator.BluePulsarConsumerGenerator.generateConsumer;
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

    private BluePulsarConsumer<IllegalMarkEvent> illegalMarkEventConsumer;

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
                        .subscribe(b ->
                                LOGGER.warn("mark jwt or ip -> SUCCESS, illegalMarkEvent = {}, b = {}", illegalMarkEvent, b));

        this.illegalMarkEventConsumer = generateConsumer(blueConsumerConfig.getByKey(ILLEGAL_MARK.name), illegalMarkEventDataConsumer);
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
        LOGGER.warn("illegalMarkEventConsumer start...");
    }

    @Override
    public void stop() {
        this.illegalMarkEventConsumer.shutdown();
        LOGGER.warn("illegalMarkEventConsumer shutdown...");
    }

}
