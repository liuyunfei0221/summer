package com.blue.auth.event.consumer;

import com.blue.auth.config.blue.BlueConsumerConfig;
import com.blue.auth.service.inter.AuthControlService;
import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.event.EmptyEvent;
import com.blue.pulsar.component.BluePulsarListener;
import org.apache.pulsar.client.api.PulsarClient;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.SYSTEM_AUTHORITY_INFOS_REFRESH;
import static com.blue.pulsar.api.generator.BluePulsarListenerGenerator.generateListener;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * refresh authority info consumer
 *
 * @author liuyunfei
 */
public final class SystemAuthorityInfosRefreshConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(SystemAuthorityInfosRefreshConsumer.class);

    private final PulsarClient pulsarClient;

    private final BlueConsumerConfig blueConsumerConfig;

    private final Scheduler scheduler;

    private final AuthControlService authControlService;

    private BluePulsarListener<EmptyEvent> pulsarListener;

    public SystemAuthorityInfosRefreshConsumer(PulsarClient pulsarClient, BlueConsumerConfig blueConsumerConfig, Scheduler scheduler, AuthControlService authControlService) {
        this.pulsarClient = pulsarClient;
        this.blueConsumerConfig = blueConsumerConfig;
        this.scheduler = scheduler;
        this.authControlService = authControlService;
    }

    @PostConstruct
    private void init() {
        Consumer<EmptyEvent> dataConsumer = emptyEvent ->
                ofNullable(emptyEvent)
                        .ifPresent(ee -> just(ee).publishOn(scheduler)
                                .then(authControlService.refreshSystemAuthorityInfos())
                                .doOnError(throwable -> LOGGER.info("authControlService.refreshSystemAuthorityInfos() failed, ee = {}, throwable = {}", ee, throwable))
                                .subscribe(ig -> LOGGER.info("authControlService.refreshSystemAuthorityInfos(), ig = {}, ee = {}", ig, ee)));

        this.pulsarListener = generateListener(pulsarClient, blueConsumerConfig.getByKey(SYSTEM_AUTHORITY_INFOS_REFRESH.name), dataConsumer);
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
