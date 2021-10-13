package com.blue.secure.config.mq.consumer;

import com.blue.base.component.lifecycle.inter.BlueLifecycle;
import com.blue.base.model.base.NonValueParam;
import com.blue.pulsar.api.conf.ConsumerConfParams;
import com.blue.pulsar.common.BluePulsarConsumer;
import com.blue.secure.config.blue.BlueConsumerConfig;
import com.blue.secure.service.inter.SecureService;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.util.function.Consumer;

import static com.blue.base.constant.base.BlueTopic.AUTHORITY_INFOS_REFRESH;
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
public class AuthorityInfosRefreshConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(AuthExpireConsumer.class);

    private final BlueConsumerConfig blueConsumerConfig;

    private final SecureService secureService;

    private BluePulsarConsumer<NonValueParam> authorityInfosRefreshConsumer;

    public AuthorityInfosRefreshConsumer(BlueConsumerConfig blueConsumerConfig, SecureService secureService, BluePulsarConsumer<NonValueParam> authorityInfosRefreshConsumer) {
        this.blueConsumerConfig = blueConsumerConfig;
        this.secureService = secureService;
        this.authorityInfosRefreshConsumer = authorityInfosRefreshConsumer;
    }

    @PostConstruct
    private void init() {
        Consumer<NonValueParam> authorityInfosRefreshDataConsumer = nonValueParam ->
                ofNullable(nonValueParam)
                        .ifPresent(nvp -> {
                            LOGGER.info("authExpireDataConsumer received");
                            secureService.refreshAuthorityInfos();
                        });

        ConsumerConfParams authorityInfosRefreshDeploy = blueConsumerConfig.getByKey(AUTHORITY_INFOS_REFRESH.name);
        this.authorityInfosRefreshConsumer = generateConsumer(authorityInfosRefreshDeploy, authorityInfosRefreshDataConsumer);
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
        this.authorityInfosRefreshConsumer.run();
        LOGGER.warn("authorityInfosRefreshConsumer start...");
    }

    @Override
    public void stop() {
        this.authorityInfosRefreshConsumer.shutdown();
        LOGGER.warn("authorityInfosRefreshConsumer shutdown...");
    }

}
