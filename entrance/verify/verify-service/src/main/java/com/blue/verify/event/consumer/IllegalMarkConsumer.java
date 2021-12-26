package com.blue.verify.event.consumer;

import com.blue.base.component.lifecycle.inter.BlueLifecycle;
import com.blue.base.model.base.IllegalMarkEvent;
import com.blue.verify.config.blue.BlueConsumerConfig;
import com.blue.verify.config.filter.global.BlueIllegalAssertFilter;
import com.blue.pulsar.common.BluePulsarConsumer;
import com.google.gson.JsonSyntaxException;
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

    private final BlueIllegalAssertFilter blueIllegalAssertFilter;

    private final BlueConsumerConfig blueConsumerConfig;

    private BluePulsarConsumer<IllegalMarkEvent> illegalMarkEventConsumer;

    public IllegalMarkConsumer(BlueIllegalAssertFilter blueIllegalAssertFilter, BlueConsumerConfig blueConsumerConfig) {
        this.blueIllegalAssertFilter = blueIllegalAssertFilter;
        this.blueConsumerConfig = blueConsumerConfig;
    }

    @PostConstruct
    private void init() {
        Consumer<IllegalMarkEvent> illegalMarkEventDataConsumer = illegalMarkEvent ->
                ofNullable(illegalMarkEvent)
                        .ifPresent(ime -> {
                            LOGGER.info("illegalMarkEventDataConsumer received, ime = {}", ime);

                            try {
                                ofNullable(ime.getJwt())
                                        .filter(jwt -> !"".equals(jwt))
                                        .ifPresent(jwt -> blueIllegalAssertFilter.markIllegalJwt(jwt,
                                                ofNullable(ime.getMark()).orElse(false)));
                                ofNullable(ime.getIp())
                                        .filter(ip -> !"".equals(ip))
                                        .ifPresent(ip -> blueIllegalAssertFilter.markIllegalIp(ip,
                                                ofNullable(ime.getMark()).orElse(false)));

                                LOGGER.warn("mark jwt or ip -> SUCCESS,ime = {}", ime);
                            } catch (JsonSyntaxException e) {
                                LOGGER.error("mark jwt or ip -> FAILED,ime = {}", ime);
                            }
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
