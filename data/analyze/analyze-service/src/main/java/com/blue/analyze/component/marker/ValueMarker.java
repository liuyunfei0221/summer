package com.blue.analyze.component.marker;

import com.blue.base.model.exps.BlueException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.blue.base.common.base.BlueCheck.isNotBlank;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.INVALID_IDENTITY;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * value marker
 *
 * @author liuyunfei
 * @date 2021/9/6
 * @apiNote
 */
@SuppressWarnings({"JavaDoc"})
@Component
public final class ValueMarker {

    private static final Logger LOGGER = getLogger(ValueMarker.class);

    private StringRedisTemplate stringRedisTemplate;

    public ValueMarker(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    private final Consumer<String> REDIS_KEY_DELETER = key -> {
        try {
            stringRedisTemplate.delete(key);
        } catch (Exception e) {
            LOGGER.error("REDIS_KEY_DELETER -> key delete failed, key = {}, e = {}", key, e);
        }
    };

    private static final String UNION_PF_KEY_PREFIX = "union_pf_key_";

    private static final int RAN_KEY_STR_LEN = 6;

    public static final Supplier<String> UNION_PF_KEY_GETTER = () ->
            UNION_PF_KEY_PREFIX + randomAlphanumeric(RAN_KEY_STR_LEN);

    /**
     * mark
     *
     * @param key
     * @param value
     * @return
     */
    public Mono<Boolean> mark(String key, String value) {
        LOGGER.info("Mono<Boolean> mark(String key, String value), key = {}, value = {}", key, value);

        return isNotBlank(key) && isNotBlank(value) ?
                just(stringRedisTemplate.opsForHyperLogLog().add(key, value) > 0L)
                :
                error(() -> new BlueException(BAD_REQUEST));
    }

    /**
     * count
     *
     * @param key
     * @return
     */
    public Mono<Long> count(String key) {
        LOGGER.info("Mono<Long> count(String key), key = {}", key);
        return isNotBlank(key) ? just(stringRedisTemplate.opsForHyperLogLog().size(key))
                :
                Mono.error(() -> new BlueException(INVALID_IDENTITY));
    }

    /**
     * merge count
     *
     * @param keys
     * @return
     */
    public Mono<Long> mergeCount(List<String> keys) {
        LOGGER.info("Mono<Long> mergeCount(List<String> keys), keys = {}", keys);
        return ofNullable(keys)
                .filter(ks -> ks.size() > 0)
                .map(ks -> {
                    String tempKey = UNION_PF_KEY_GETTER.get();
                    return just(stringRedisTemplate.opsForHyperLogLog().union(tempKey,
                            ks.toArray(String[]::new)))
                            .doFinally(signalType -> REDIS_KEY_DELETER.accept(tempKey));
                }).orElseGet(() -> just(0L));
    }

}
