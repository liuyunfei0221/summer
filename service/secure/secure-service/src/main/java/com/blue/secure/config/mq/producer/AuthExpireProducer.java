package com.blue.secure.config.mq.producer;

import com.blue.base.component.lifecycle.inter.BlueLifecycle;
import com.blue.base.constant.base.BlueTopic;
import com.blue.base.model.redis.KeyExpireParam;
import com.blue.pulsar.common.BluePulsarProducer;
import com.blue.secure.config.blue.BlueProducerConfig;
import org.apache.pulsar.client.api.MessageId;
import reactor.util.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.pulsar.api.generator.BluePulsarProducerGenerator.generateProducer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static reactor.util.Loggers.getLogger;


/**
 * jwt过期时间刷新生产者
 *
 * @author DarkBlue
 */
@SuppressWarnings({"Duplicates", "JavaDoc"})
public final class AuthExpireProducer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(AuthExpireProducer.class);

    private final ExecutorService executorService;

    private final BluePulsarProducer<KeyExpireParam> authExpireProducer;

    public AuthExpireProducer(ExecutorService executorService, BlueProducerConfig blueProducerConfig) {
        this.executorService = executorService;
        this.authExpireProducer = generateProducer(blueProducerConfig.getByKey(BlueTopic.AUTH_EXPIRE.name), KeyExpireParam.class);
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
        LOGGER.warn("authExpireProducer start...");
    }

    @Override
    public void stop() {
        this.authExpireProducer.shutdown();
        LOGGER.warn("authExpireProducer shutdown...");
    }

    /**
     * 发送消息
     *
     * @param keyExpireParam
     */
    public void send(KeyExpireParam keyExpireParam) {
        CompletableFuture<MessageId> future = authExpireProducer.sendAsync(keyExpireParam);
        LOGGER.info("authExpireProducer send, keyExpireParam = {}", keyExpireParam);
        Consumer<MessageId> c = messageId ->
                LOGGER.info("authExpireProducer send success, keyExpireParam = {}, messageId = {}", keyExpireParam, messageId);
        future.thenAcceptAsync(c, executorService);
    }

}
