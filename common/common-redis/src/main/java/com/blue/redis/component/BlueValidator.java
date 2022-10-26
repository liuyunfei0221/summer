package com.blue.redis.component;

import com.blue.basic.model.exps.BlueException;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.redis.api.generator.BlueRedisScriptGenerator.generateScriptByScriptStr;
import static com.blue.redis.constant.RedisScripts.REPEATABLE_UNTIL_SUCCESS_OR_TIMEOUT_VALIDATION;
import static com.blue.redis.constant.RedisScripts.UNREPEATABLE_VALIDATION;
import static java.util.Collections.singletonList;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;

/**
 * validator
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc", "unused"})
public final class BlueValidator {

    private final ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    public BlueValidator(ReactiveStringRedisTemplate reactiveStringRedisTemplate) {
        if (isNull(reactiveStringRedisTemplate))
            throw new RuntimeException("reactiveStringRedisTemplate can't be null");

        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
    }

    private static final String KEY_PREFIX = "VK_";

    private static final UnaryOperator<String> KEY_WRAPPER = key ->
            KEY_PREFIX + key;

    private static final Function<String, List<String>> SCRIPT_KEYS_WRAPPER = key ->
            singletonList(KEY_WRAPPER.apply(key));

    private final Function<String, List<String>> SCRIPT_ARGS_WRAPPER = Collections::singletonList;

    private static final Function<Throwable, Flux<Boolean>> FALL_BACKER = e ->
            Flux.just(true);

    private static final RedisScript<Boolean> UNREPEATABLE_VALIDATION_SCRIPT = generateScriptByScriptStr(UNREPEATABLE_VALIDATION.str, Boolean.class);

    private static final RedisScript<Boolean> REPEATABLE_UNTIL_SUCCESS_OR_TIMEOUT_VALIDATION_SCRIPT =
            generateScriptByScriptStr(REPEATABLE_UNTIL_SUCCESS_OR_TIMEOUT_VALIDATION.str, Boolean.class);

    /**
     * set k-v with expire
     *
     * @param key
     * @param value
     * @param expire
     * @return
     */
    public Mono<Boolean> setKeyValueWithExpire(String key, String value, Duration expire) {
        assertParam(key, value);

        return isNotNull(expire) ?
                reactiveStringRedisTemplate.opsForValue()
                        .set(KEY_WRAPPER.apply(key), value, expire)
                        
                :
                reactiveStringRedisTemplate.opsForValue()
                        .set(KEY_WRAPPER.apply(key), value)
                        ;
    }

    /**
     * unrepeatable validate
     *
     * @param key
     * @param value
     * @return
     */
    public Mono<Boolean> unRepeatableValidate(String key, String value) {
        assertParam(key, value);

        return reactiveStringRedisTemplate.execute(UNREPEATABLE_VALIDATION_SCRIPT,
                        SCRIPT_KEYS_WRAPPER.apply(key), SCRIPT_ARGS_WRAPPER.apply(value))
                .onErrorResume(FALL_BACKER)
                
                .elementAt(0);
    }

    /**
     * repeatable validate until success or timeout
     *
     * @param key
     * @param value
     * @return
     */
    public Mono<Boolean> repeatableValidateUntilSuccessOrTimeout(String key, String value) {
        assertParam(key, value);

        return reactiveStringRedisTemplate.execute(REPEATABLE_UNTIL_SUCCESS_OR_TIMEOUT_VALIDATION_SCRIPT,
                        SCRIPT_KEYS_WRAPPER.apply(key), SCRIPT_ARGS_WRAPPER.apply(value))
                .onErrorResume(FALL_BACKER)
                
                .elementAt(0);
    }

    /**
     * repeatable validate until timeout
     *
     * @param key
     * @param value
     * @return
     */
    public Mono<Boolean> repeatableValidateUntilTimeout(String key, String value) {
        assertParam(key, value);

        return reactiveStringRedisTemplate.opsForValue().get(KEY_WRAPPER.apply(key))
                
                .flatMap(v -> just(value.equals(v)));
    }

    /**
     * delete key
     *
     * @param key
     * @return
     */
    public Mono<Boolean> delete(String key) {
        return isNotBlank(key) ?
                reactiveStringRedisTemplate.delete(key)
                        .flatMap(r -> just(r > 0))
                :
                error(() -> new BlueException(EMPTY_PARAM));
    }

    /**
     * assert params
     *
     * @param key
     * @param value
     */
    private void assertParam(String key, String value) {
        if (isNull(key))
            throw new BlueException(EMPTY_PARAM);
        if (isNull(value))
            throw new BlueException(EMPTY_PARAM);
    }

}
