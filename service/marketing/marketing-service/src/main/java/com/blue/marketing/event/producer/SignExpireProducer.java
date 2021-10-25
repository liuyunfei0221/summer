package com.blue.marketing.event.producer;

import com.blue.base.component.lifecycle.inter.BlueLifecycle;
import com.blue.base.model.base.KeyExpireParam;
import com.blue.marketing.config.blue.BlueProducerConfig;
import com.blue.pulsar.common.BluePulsarProducer;
import org.apache.pulsar.client.api.MessageId;
import reactor.util.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.base.constant.base.BlueTopic.SIGN_EXPIRE;
import static com.blue.pulsar.api.generator.BluePulsarProducerGenerator.generateProducer;
import static reactor.util.Loggers.getLogger;


/**
 * sign in info expire producer
 *
 * @author DarkBlue
 */
@SuppressWarnings({"Duplicates", "JavaDoc"})
public class SignExpireProducer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(SignExpireProducer.class);

    private final ExecutorService executorService;

    private final BluePulsarProducer<KeyExpireParam> signExpireProducer;

    public SignExpireProducer(ExecutorService executorService, BlueProducerConfig blueProducerConfig) {
        this.executorService = executorService;
        this.signExpireProducer = generateProducer(blueProducerConfig.getByKey(SIGN_EXPIRE.name), KeyExpireParam.class);
    }

    @Override
    public int startPrecedence() {
        return Integer.MIN_VALUE;
    }

    @Override
    public int stopPrecedence() {
        return Integer.MIN_VALUE;
    }

    @Override
    public void start() {
        LOGGER.warn("signExpireProducer start...");
    }

    @Override
    public void stop() {
        this.signExpireProducer.shutdown();
        LOGGER.warn("signExpireProducer shutdown...");
    }

    /**
     * send event
     *
     * @param keyExpireParam
     */
    public void send(KeyExpireParam keyExpireParam) {
        CompletableFuture<MessageId> future = signExpireProducer.sendAsync(keyExpireParam);

        LOGGER.info("signExpireProducer send, keyExpireParam = {}", keyExpireParam);
        Consumer<MessageId> c = messageId ->
                LOGGER.info("signExpireProducer send success, keyExpireParam = {}, messageId = {}", keyExpireParam, messageId);

        future.thenAcceptAsync(c, executorService);
    }

}
