package com.blue.secure.event.consumer;

import com.blue.base.component.lifecycle.inter.BlueLifecycle;
import com.blue.base.model.base.NonValueParam;
import com.blue.pulsar.common.BluePulsarConsumer;
import com.blue.secure.config.blue.BlueConsumerConfig;
import com.blue.secure.service.inter.ControlService;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.base.constant.base.BlueTopic.SYSTEM_AUTHORITY_INFOS_REFRESH;
import static com.blue.pulsar.api.generator.BluePulsarConsumerGenerator.generateConsumer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * @author liuyunfei
 * @date 2021/10/14
 * @apiNote
 */
public class SystemAuthorityInfosRefreshConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(SystemAuthorityInfosRefreshConsumer.class);

    private final BlueConsumerConfig blueConsumerConfig;

    private final ControlService controlService;

    private BluePulsarConsumer<NonValueParam> systemAuthorityInfosRefreshConsumer;

    public SystemAuthorityInfosRefreshConsumer(BlueConsumerConfig blueConsumerConfig, ControlService controlService) {
        this.blueConsumerConfig = blueConsumerConfig;
        this.controlService = controlService;
    }

    @PostConstruct
    private void init() {
        Consumer<NonValueParam> systemAuthorityInfosRefreshDataConsumer = nonValueParam ->
                ofNullable(nonValueParam)
                        .ifPresent(nvp -> {
                            LOGGER.info("systemAuthorityInfosRefreshDataConsumer received");
                            controlService.refreshSystemAuthorityInfos();
                        });

        this.systemAuthorityInfosRefreshConsumer = generateConsumer(blueConsumerConfig.getByKey(SYSTEM_AUTHORITY_INFOS_REFRESH.name), systemAuthorityInfosRefreshDataConsumer);
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
