package com.blue.redis.common;

import com.blue.base.model.exps.BlueException;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.redis.api.generator.BlueRedisScriptGenerator.generateScriptByScriptStr;
import static com.blue.redis.constant.RedisScripts.REPEATABLE_UNTIL_SUCCESS_OR_TIMEOUT_VALIDATION;
import static com.blue.redis.constant.RedisScripts.UNREPEATABLE_VALIDATION;
import static reactor.core.publisher.Mono.just;
import static reactor.core.scheduler.Schedulers.boundedElastic;

/**
 * validator
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc"})
public final class BlueValidator {

    private final ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    private final Scheduler scheduler;

    public BlueValidator(ReactiveStringRedisTemplate reactiveStringRedisTemplate, Scheduler scheduler) {
        assertParam(reactiveStringRedisTemplate);

        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
        this.scheduler = scheduler != null ? scheduler : boundedElastic();
    }

    private static final String KEY_PREFIX = "vk_";

    private static final UnaryOperator<String> KEY_WRAPPER = key -> {
        if (key == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "key can't be null");

        return KEY_PREFIX + key;
    };

    private static final Function<String, List<String>> SCRIPT_KEYS_WRAPPER = key ->
            List.of(KEY_WRAPPER.apply(key));

    private final Function<String, List<String>> SCRIPT_ARGS_WRAPPER = value -> {
        if (value == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "value can't be null");

        return List.of(value);
    };

    private static final Function<Throwable, Flux<Boolean>> FALL_BACKER = e ->
            Flux.just(true);

    private static final RedisScript<Boolean> UNREPEATABLE_VALIDATION_SCRIPT = generateScriptByScriptStr(UNREPEATABLE_VALIDATION.str, Boolean.class);

    private static final RedisScript<Boolean> REPEATABLE_UNTIL_SUCCESS_OR_TIMEOUT_VALIDATION_SCRIPT = generateScriptByScriptStr(REPEATABLE_UNTIL_SUCCESS_OR_TIMEOUT_VALIDATION.str, Boolean.class);

    /**
     * set k-v with expire
     *
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public Mono<Boolean> setKeyValueWithExpire(String key, String value, Duration expire) {
        if (key == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "key can't be null");
        if (value == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "value can't be null");
        if (expire == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "expire can't be null");

        return reactiveStringRedisTemplate.opsForValue()
                .set(KEY_WRAPPER.apply(key), value, expire)
                .publishOn(scheduler);
    }

    /**
     * unrepeatable validate
     *
     * @param key
     * @param value
     * @return
     */
    public Mono<Boolean> unrepeatableValidate(String key, String value) {
        return reactiveStringRedisTemplate.execute(UNREPEATABLE_VALIDATION_SCRIPT,
                        SCRIPT_KEYS_WRAPPER.apply(key), SCRIPT_ARGS_WRAPPER.apply(value))
                .onErrorResume(FALL_BACKER)
                .elementAt(0)
                .publishOn(scheduler);
    }

    /**
     * repeatable validate until success or timeout
     *
     * @param key
     * @param value
     * @return
     */
    public Mono<Boolean> repeatableValidateUntilSuccessOrTimeout(String key, String value) {
        return reactiveStringRedisTemplate.execute(REPEATABLE_UNTIL_SUCCESS_OR_TIMEOUT_VALIDATION_SCRIPT,
                        SCRIPT_KEYS_WRAPPER.apply(key), SCRIPT_ARGS_WRAPPER.apply(value))
                .onErrorResume(FALL_BACKER)
                .elementAt(0)
                .publishOn(scheduler);
    }

    /**
     * repeatable validate until timeout
     *
     * @param key
     * @param value
     * @return
     */
    public Mono<Boolean> repeatableValidateUntilTimeout(String key, String value) {
        if (value == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "value can't be null");

        return reactiveStringRedisTemplate.opsForValue().get(KEY_WRAPPER.apply(key))
                .flatMap(v -> just(value.equals(v)))
                .publishOn(scheduler);
    }

    /**
     * assert params
     *
     * @param reactiveStringRedisTemplate
     */
    private void assertParam(ReactiveStringRedisTemplate reactiveStringRedisTemplate) {
        if (reactiveStringRedisTemplate == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "reactiveStringRedisTemplate can't be null");
    }

}
