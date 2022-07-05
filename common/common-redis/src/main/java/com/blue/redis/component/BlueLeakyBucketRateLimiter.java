package com.blue.redis.component;

import com.blue.base.model.exps.BlueException;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.constant.common.ResponseElement.BAD_REQUEST;
import static com.blue.redis.api.generator.BlueRedisScriptGenerator.generateScriptByScriptStr;
import static com.blue.redis.constant.RedisScripts.LEAKY_BUCKET_RATE_LIMITER;
import static java.util.Arrays.asList;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static reactor.core.scheduler.Schedulers.boundedElastic;

/**
 * leaky bucket rate limiter
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "unused"})
public final class BlueLeakyBucketRateLimiter {

    private final ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    private final Scheduler scheduler;

    public BlueLeakyBucketRateLimiter(ReactiveStringRedisTemplate reactiveStringRedisTemplate, Scheduler scheduler) {
        if (isNull(reactiveStringRedisTemplate))
            throw new RuntimeException("reactiveStringRedisTemplate can't be null");

        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
        this.scheduler = isNotNull(scheduler) ? scheduler : boundedElastic();
    }

    private static final RedisScript<Boolean> SCRIPT = generateScriptByScriptStr(LEAKY_BUCKET_RATE_LIMITER.str, Boolean.class);

    private static final String KEY_PREFIX = "LB_RLI_";

    private static final UnaryOperator<String> KEY_WRAPPER = key ->
            KEY_PREFIX + key;

    private static final Function<String, List<String>> SCRIPT_KEYS_WRAPPER = id ->
            List.of(KEY_WRAPPER.apply(id));

    private final BiFunction<Integer, Long, List<String>> SCRIPT_ARGS_WRAPPER = (allow, ttl) ->
            asList(String.valueOf(allow), String.valueOf(ttl));

    private static final Function<Throwable, Flux<Boolean>> FALL_BACKER = e ->
            Flux.just(true);

    /**
     * key allowed?
     *
     * @param limitKey
     * @param allow
     * @param expiresMillis
     * @return
     */
    public Mono<Boolean> isAllowed(String limitKey, Integer allow, Long expiresMillis) {
        assertParam(limitKey, allow, expiresMillis);

        return reactiveStringRedisTemplate.execute(SCRIPT, SCRIPT_KEYS_WRAPPER.apply(limitKey),
                        SCRIPT_ARGS_WRAPPER.apply(allow, expiresMillis))
                .publishOn(scheduler)
                .onErrorResume(FALL_BACKER)
                .elementAt(0);
    }

    /**
     * key allowed?
     *
     * @param limitKey
     * @param allow
     * @param expiresMillis
     * @return
     */
    public Boolean isAllowedBySync(String limitKey, Integer allow, Long expiresMillis) {
        return isAllowed(limitKey, allow, expiresMillis).toFuture().join();
    }

    /**
     * delete key
     *
     * @param key
     * @return
     */
    public Mono<Boolean> delete(String key) {
        return isNotBlank(key) ?
                reactiveStringRedisTemplate.delete(key).publishOn(scheduler)
                        .flatMap(r -> just(r > 0))
                :
                error(() -> new BlueException(BAD_REQUEST));
    }

    /**
     * assert params
     *
     * @param limitKey
     * @param allow
     * @param expiresMillis
     */
    private void assertParam(String limitKey, Integer allow, Long expiresMillis) {
        if (isBlank(limitKey))
            throw new RuntimeException("limitKey can't be null");

        if (isNull(allow) || isNull(expiresMillis) || allow < 1 || expiresMillis < 1L)
            throw new RuntimeException("allow and expiresMillis can't be null or less than 1");
    }

}
