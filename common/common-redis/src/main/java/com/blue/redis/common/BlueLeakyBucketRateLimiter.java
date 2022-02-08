package com.blue.redis.common;

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

import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.redis.api.generator.BlueRedisScriptGenerator.generateScriptByScriptStr;
import static com.blue.redis.constant.RedisScripts.LEAKY_BUCKET_RATE_LIMITER;
import static java.util.Arrays.asList;
import static reactor.core.scheduler.Schedulers.boundedElastic;

/**
 * leaky bucket rate limiter
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
public final class BlueLeakyBucketRateLimiter {

    private final ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    private final Scheduler scheduler;

    public BlueLeakyBucketRateLimiter(ReactiveStringRedisTemplate reactiveStringRedisTemplate, Scheduler scheduler) {
        assertParam(reactiveStringRedisTemplate);

        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
        this.scheduler = scheduler != null ? scheduler : boundedElastic();
    }

    private static final RedisScript<Boolean> SCRIPT = generateScriptByScriptStr(LEAKY_BUCKET_RATE_LIMITER.str, Boolean.class);

    private static final String KEY_PREFIX = "lb_rli_";

    private static final UnaryOperator<String> KEY_WRAPPER = key -> {
        if (key == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "key can't be null");

        return KEY_PREFIX + key;
    };

    private static final Function<String, List<String>> SCRIPT_KEYS_WRAPPER = id ->
            List.of(KEY_WRAPPER.apply(id));

    private final BiFunction<Integer, Long, List<String>> SCRIPT_ARGS_WRAPPER = (allow, ttl) -> {
        if (allow == null || ttl == null || allow < 1 || ttl < 1L)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "allow and ttl can't be null or less than 1");

        return asList(String.valueOf(allow), String.valueOf(ttl));
    };

    private static final Function<Throwable, Flux<Boolean>> FALL_BACKER = e ->
            Flux.just(true);

    /**
     * key allowed?
     *
     * @param limitKey
     * @return
     */
    public Mono<Boolean> isAllowed(String limitKey, Integer allow, Long expireMillis) {
        return reactiveStringRedisTemplate.execute(SCRIPT, SCRIPT_KEYS_WRAPPER.apply(limitKey),
                        SCRIPT_ARGS_WRAPPER.apply(allow, expireMillis))
                .onErrorResume(FALL_BACKER)
                .elementAt(0)
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
