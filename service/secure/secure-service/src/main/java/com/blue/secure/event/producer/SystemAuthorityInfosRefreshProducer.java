package com.blue.secure.event.producer;//package com.blue.secure.config.mq.api.producer;

import com.blue.base.component.lifecycle.inter.BlueLifecycle;
import com.blue.base.model.base.NonValueParam;
import com.blue.pulsar.common.BluePulsarProducer;
import com.blue.secure.config.blue.BlueProducerConfig;
import org.apache.pulsar.client.api.MessageId;
import reactor.util.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.base.constant.base.BlueTopic.SYSTEM_AUTHORITY_INFOS_REFRESH;
import static com.blue.pulsar.api.generator.BluePulsarProducerGenerator.generateProducer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static reactor.util.Loggers.getLogger;

/**
 * invalid auth from local cache consumer
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public final class SystemAuthorityInfosRefreshProducer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(SystemAuthorityInfosRefreshProducer.class);

    private final ExecutorService executorService;

    private final BluePulsarProducer<NonValueParam> authorityInfosRefreshProducer;

    public SystemAuthorityInfosRefreshProducer(ExecutorService executorService, BlueProducerConfig blueProducerConfig) {
        this.executorService = executorService;
        this.authorityInfosRefreshProducer = generateProducer(blueProducerConfig.getByKey(SYSTEM_AUTHORITY_INFOS_REFRESH.name), NonValueParam.class);
    }

    @Override
    public int startPrecedence() {
        return MIN_VALUE;
    }

    @Override
    public int stopPrecedence() {
        return MAX_VALUE;
    }

    @Override
    public void start() {
        LOGGER.warn("invalidLocalAuthProducer start...");
    }

    @Override
    public void stop() {
        this.authorityInfosRefreshProducer.shutdown();
        LOGGER.warn("authorityInfosRefreshProducer shutdown...");
    }

    /**
     * send message
     *
     * @param nonValueParam
     */
    public void send(NonValueParam nonValueParam) {
        CompletableFuture<MessageId> future = authorityInfosRefreshProducer.sendAsync(nonValueParam);
        LOGGER.info("authorityInfosRefreshProducer send, nonValueParam = {}", nonValueParam);
        Consumer<MessageId> c = messageId ->
                LOGGER.info("authorityInfosRefreshProducer send success, nonValueParam = {}, messageId = {}", nonValueParam, messageId);
        future.thenAcceptAsync(c, executorService);
    }

}
