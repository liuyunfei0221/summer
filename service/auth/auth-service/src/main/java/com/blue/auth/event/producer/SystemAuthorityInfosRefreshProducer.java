package com.blue.auth.event.producer;

import com.blue.auth.config.blue.BlueProducerConfig;
import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.common.EmptyEvent;
import com.blue.pulsar.common.BluePulsarProducer;
import org.apache.pulsar.client.api.MessageId;
import reactor.util.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.SYSTEM_AUTHORITY_INFOS_REFRESH;
import static com.blue.pulsar.api.generator.BluePulsarProducerGenerator.generateProducer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static reactor.util.Loggers.getLogger;

/**
 * refresh authority info producer
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public final class SystemAuthorityInfosRefreshProducer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(SystemAuthorityInfosRefreshProducer.class);

    private final ExecutorService executorService;

    private final BluePulsarProducer<EmptyEvent> authorityInfosRefreshProducer;

    public SystemAuthorityInfosRefreshProducer(ExecutorService executorService, BlueProducerConfig blueProducerConfig) {
        this.executorService = executorService;
        this.authorityInfosRefreshProducer = generateProducer(blueProducerConfig.getByKey(SYSTEM_AUTHORITY_INFOS_REFRESH.name), EmptyEvent.class);
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
        this.authorityInfosRefreshProducer.flush();
        this.authorityInfosRefreshProducer.close();
        LOGGER.warn("authorityInfosRefreshProducer shutdown...");
    }

    /**
     * send message
     *
     * @param emptyEvent
     */
    public void send(EmptyEvent emptyEvent) {
        CompletableFuture<MessageId> future = authorityInfosRefreshProducer.sendAsync(emptyEvent);
        LOGGER.info("authorityInfosRefreshProducer send, nonValueParam = {}", emptyEvent);
        Consumer<MessageId> c = messageId ->
                LOGGER.info("authorityInfosRefreshProducer send success, nonValueParam = {}, messageId = {}", emptyEvent, messageId);
        future.thenAcceptAsync(c, executorService);
    }

}
