package com.blue.auth.event.consumer;

import com.blue.auth.config.blue.BlueConsumerConfig;
import com.blue.auth.service.inter.AuthControlService;
import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.event.EmptyEvent;
import com.blue.pulsar.api.generator.BluePulsarListenerGenerator;
import com.blue.pulsar.common.BluePulsarListener;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.SYSTEM_AUTHORITY_INFOS_REFRESH;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * refresh authority info consumer
 *
 * @author liuyunfei
 */
public final class SystemAuthorityInfosRefreshConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(SystemAuthorityInfosRefreshConsumer.class);

    private final BlueConsumerConfig blueConsumerConfig;

    private final AuthControlService authControlService;

    private BluePulsarListener<EmptyEvent> systemAuthorityInfosRefreshConsumer;

    public SystemAuthorityInfosRefreshConsumer(BlueConsumerConfig blueConsumerConfig, AuthControlService authControlService) {
        this.blueConsumerConfig = blueConsumerConfig;
        this.authControlService = authControlService;
    }

    @PostConstruct
    private void init() {
        Consumer<EmptyEvent> systemAuthorityInfosRefreshDataConsumer = emptyEvent ->
                ofNullable(emptyEvent)
                        .ifPresent(ee -> {
                            LOGGER.info("systemAuthorityInfosRefreshDataConsumer received");
                            authControlService.refreshSystemAuthorityInfos()
                                    .doOnError(throwable -> LOGGER.info("controlService.refreshSystemAuthorityInfos() failed, throwable = {}", throwable))
                                    .subscribe(v -> LOGGER.info("controlService.refreshSystemAuthorityInfos()"));
                        });

        this.systemAuthorityInfosRefreshConsumer = BluePulsarListenerGenerator.generateListener(blueConsumerConfig.getByKey(SYSTEM_AUTHORITY_INFOS_REFRESH.name), systemAuthorityInfosRefreshDataConsumer);
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
        this.systemAuthorityInfosRefreshConsumer.run();
        LOGGER.warn("systemAuthorityInfosRefreshConsumer start...");
    }

    @Override
    public void stop() {
        this.systemAuthorityInfosRefreshConsumer.shutdown();
        LOGGER.warn("systemAuthorityInfosRefreshConsumer shutdown...");
    }

}
