package com.blue.marketing.event.consumer;

import com.blue.base.component.lifecycle.inter.BlueLifecycle;
import com.blue.base.model.base.KeyExpireParam;
import com.blue.marketing.config.blue.BlueConsumerConfig;
import com.blue.pulsar.common.BluePulsarConsumer;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.function.Consumer;

import static com.blue.base.constant.base.BlueTopic.SIGN_EXPIRE;
import static com.blue.pulsar.api.generator.BluePulsarConsumerGenerator.generateConsumer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * sign in info expire consumer
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class SignExpireConsumer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(SignExpireConsumer.class);

    private final ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    private final BlueConsumerConfig blueConsumerConfig;

    private BluePulsarConsumer<KeyExpireParam> signExpireConsumer;

    public SignExpireConsumer(ReactiveStringRedisTemplate reactiveStringRedisTemplate, BlueConsumerConfig blueConsumerConfig) {
        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
        this.blueConsumerConfig = blueConsumerConfig;
    }

    @PostConstruct
    private void init() {
        Consumer<KeyExpireParam> signExpireDataConsumer = keyExpireParam ->
                ofNullable(keyExpireParam)
                        .ifPresent(kep -> {
                            LOGGER.info("marketingDataConsumer received, kep = {}", kep);

                            String key = kep.getKey();
                            Long expire = kep.getExpire();
                            ChronoUnit unit = kep.getUnit();

                            reactiveStringRedisTemplate.expire(key, Duration.of(expire, unit))
                                    .subscribe(b -> {
                                        if (b) {
                                            LOGGER.warn("EXPIRE_SETTING() -> SUCCESS,key = {},expire = {},unit = {}", key, expire, unit);
                                        } else {
                                            LOGGER.warn("EXPIRE_SETTING() -> FAILED,key = {},expire = {},unit = {}", key, expire, unit);
                                        }
                                    });
                        });

        this.signExpireConsumer = generateConsumer(blueConsumerConfig.getByKey(SIGN_EXPIRE.name), signExpireDataConsumer);
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
        this.signExpireConsumer.run();
        LOGGER.warn("signExpireConsumer start...");
    }

    @Override
    public void stop() {
        this.signExpireConsumer.shutdown();
        LOGGER.warn("signExpireConsumer shutdown...");
    }

}