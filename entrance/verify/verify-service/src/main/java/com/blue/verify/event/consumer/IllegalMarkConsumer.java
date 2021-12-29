package com.blue.verify.event.consumer;

import com.blue.base.component.lifecycle.inter.BlueLifecycle;
import com.blue.base.model.base.IllegalMarkEvent;
import com.blue.pulsar.common.BluePulsarConsumer;
import com.blue.verify.component.IllegalAsserter;
import com.blue.verify.config.blue.BlueConsumerConfig;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.base.constant.base.BlueTopic.ILLEGAL_MARK;
import static com.blue.pulsar.api.generator.BluePulsarConsumerGenerator.generateConsumer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * illegal mark consumer
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class IllegalMarkConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(IllegalMarkConsumer.class);

    private final IllegalAsserter illegalAsserter;

    private final BlueConsumerConfig blueConsumerConfig;

    private BluePulsarConsumer<IllegalMarkEvent> illegalMarkEventConsumer;

    public IllegalMarkConsumer(IllegalAsserter illegalAsserter, BlueConsumerConfig blueConsumerConfig) {
        this.illegalAsserter = illegalAsserter;
        this.blueConsumerConfig = blueConsumerConfig;
    }

    @PostConstruct
    private void init() {
        Consumer<IllegalMarkEvent> illegalMarkEventDataConsumer = illegalMarkEvent ->
                ofNullable(illegalMarkEvent)
                        .ifPresent(ime -> {
                            LOGGER.info("illegalMarkEventDataConsumer received, ime = {}", ime);
                            illegalAsserter.handleIllegalMarkEvent(ime).subscribe(b -> {
                                if (b) {
                                    LOGGER.info("mark jwt or ip -> SUCCESS, ime = {}", ime);
                                } else {
                                    LOGGER.error("mark jwt or ip -> FAILED, ime = {}", ime);
                                }
                            });
                        });

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
        LOGGER.warn("invalidClusterLocalAuthConsumer start...");
    }

    @Override
    public void stop() {
        this.illegalMarkEventConsumer.shutdown();
        LOGGER.warn("invalidClusterLocalAuthConsumer shutdown...");
    }

}
