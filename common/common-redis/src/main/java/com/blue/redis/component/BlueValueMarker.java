package com.blue.redis.component;

import com.blue.basic.model.exps.BlueException;
import org.slf4j.Logger;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.BlueCommonThreshold.DB_SELECT;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;
import static com.blue.redis.api.generator.BlueRedisScriptGenerator.generateScriptByScriptStr;
import static com.blue.redis.constant.RedisScripts.EXPIRE_HLL_OR_WITH_INIT;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.slf4j.LoggerFactory.getLogger;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;

/**
 * value marker
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class BlueValueMarker {

    private static final Logger LOGGER = getLogger(BlueValueMarker.class);

    private StringRedisTemplate stringRedisTemplate;

    public BlueValueMarker(StringRedisTemplate stringRedisTemplate) {
        if (isNull(stringRedisTemplate))
            throw new RuntimeException("stringRedisTemplate can't be null");

        this.stringRedisTemplate = stringRedisTemplate;
    }

    private final Consumer<String> REDIS_KEY_DELETER = key -> {
        try {
            stringRedisTemplate.delete(key);
        } catch (Exception e) {
            LOGGER.error("REDIS_KEY_DELETER -> key delete failed, key = {}, e = {}", key, e);
        }
    };

    private static final RedisScript<Boolean> EXPIRE_HLL_OR_WITH_INIT_SCRIPT = generateScriptByScriptStr(EXPIRE_HLL_OR_WITH_INIT.str, Boolean.class);

    private static final Function<String, List<String>> SCRIPT_KEYS_WRAPPER = Collections::singletonList;

    private static final BiFunction<String, Long, Object[]> SCRIPT_ARGS_WRAPPER = (v, expire) ->
            new Object[]{v, String.valueOf(expire)};

    private boolean expireHllOrWithInit(String key, String initValue, Long expiresMillis) {
        if (isNotBlank(key) && isNotBlank(initValue) && isNotNull(expiresMillis) && expiresMillis > 0L)
            return ofNullable(stringRedisTemplate.execute(EXPIRE_HLL_OR_WITH_INIT_SCRIPT, SCRIPT_KEYS_WRAPPER.apply(key),
                    SCRIPT_ARGS_WRAPPER.apply(initValue, expiresMillis))).orElse(false);

        throw new RuntimeException("key or initValue can't be blank, expiresMillis can't be null or less than 1");
    }


    private static final String UNION_PF_KEY_PREFIX = "UNION_PF_KEY_";

    private static final int RAN_KEY_STR_LEN = 6;

    public static final Supplier<String> UNION_PF_KEY_GETTER = () ->
            UNION_PF_KEY_PREFIX + randomAlphanumeric(RAN_KEY_STR_LEN);

    /**
     * if HyperLogLog exist, expire. or init, then expire
     *
     * @param key
     * @param expiresMillis
     * @return
     */
    public Mono<Boolean> expireKeyOrWithInit(String key, String initValue, Long expiresMillis) {
        LOGGER.info("Mono<Boolean> expireKeyOrWithInit(String key, Long expiresMillis), key = {}, expiresMillis = {}", key, expiresMillis);

        return just(expireHllOrWithInit(key, initValue, expiresMillis));
    }

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
                error(() -> new BlueException(EMPTY_PARAM));
    }

    /**
     * count
     *
     * @param keys
     * @return
     */
    public Mono<Long> count(String... keys) {
        LOGGER.info("Mono<Long> count(String key), key = {}", Stream.of(keys).collect(toList()));

        int len = keys.length;
        return len >= 1 && len < (int) DB_SELECT.value ? just(stringRedisTemplate.opsForHyperLogLog().size(keys))
                :
                error(() -> new BlueException(INVALID_IDENTITY));
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

    /**
     * delete key
     *
     * @param key
     * @return
     */
    public Mono<Boolean> delete(String key) {
        return isNotBlank(key) ?
                just(ofNullable(stringRedisTemplate.delete(key)).orElse(false))
                :
                error(() -> new BlueException(EMPTY_PARAM));
    }

}
