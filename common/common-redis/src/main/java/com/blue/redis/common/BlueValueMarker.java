package com.blue.redis.common;

import com.blue.base.model.exps.BlueException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.constant.base.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.redis.api.generator.BlueRedisScriptGenerator.generateScriptByScriptStr;
import static com.blue.redis.constant.RedisScripts.EXPIRE_HLL_OR_WITH_INIT;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
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
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class BlueValueMarker {

    private static final Logger LOGGER = getLogger(BlueValueMarker.class);

    private StringRedisTemplate stringRedisTemplate;

    public BlueValueMarker(StringRedisTemplate stringRedisTemplate) {
        assertParam(stringRedisTemplate);

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

    private static final Function<String, List<String>> SCRIPT_KEYS_WRAPPER = Arrays::asList;

    private static final BiFunction<String, Long, Object[]> SCRIPT_ARGS_WRAPPER = (v, expire) ->
            new Object[]{v, String.valueOf(expire)};

    private boolean expireHllOrWithInit(String key, String initValue, Long expireMillis) {
        if (isNotBlank(key) && isNotBlank(initValue) && isNotNull(expireMillis) && expireMillis > 0L)
            return ofNullable(stringRedisTemplate.execute(EXPIRE_HLL_OR_WITH_INIT_SCRIPT, SCRIPT_KEYS_WRAPPER.apply(key),
                    SCRIPT_ARGS_WRAPPER.apply(initValue, expireMillis))).orElse(false);

        throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "key or initValue can't be blank, expireMillis can't be null or less than 1");
    }


    private static final String UNION_PF_KEY_PREFIX = "union_pf_key_";

    private static final int RAN_KEY_STR_LEN = 6;

    public static final Supplier<String> UNION_PF_KEY_GETTER = () ->
            UNION_PF_KEY_PREFIX + randomAlphanumeric(RAN_KEY_STR_LEN);

    /**
     * if HyperLogLog exist, expire. or init, then expire
     *
     * @param key
     * @param expireMillis
     * @return
     */
    public Mono<Boolean> expireKeyOrWithInit(String key, String initValue, Long expireMillis) {
        LOGGER.info("Mono<Boolean> expireKeyOrWithInit(String key, Long expireMillis), key = {}, expireMillis = {}", key, expireMillis);

        return just(expireHllOrWithInit(key, initValue, expireMillis));
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
                error(() -> new BlueException(BAD_REQUEST));
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
                error(() -> new BlueException(BAD_REQUEST));
    }

    /**
     * assert params
     *
     * @param stringRedisTemplate
     */
    private void assertParam(StringRedisTemplate stringRedisTemplate) {
        if (isNull(stringRedisTemplate))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "stringRedisTemplate can't be null");
    }

}
