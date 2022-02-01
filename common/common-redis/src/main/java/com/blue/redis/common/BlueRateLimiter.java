package com.blue.redis.common;

import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.function.Supplier;

import static com.blue.redis.api.generator.BlueRedisScriptGenerator.generateScriptByScriptStr;
import static com.blue.redis.constant.RedisScripts.TOKEN_BUCKET_RATE_LIMITER;
import static java.lang.System.currentTimeMillis;
import static reactor.core.scheduler.Schedulers.boundedElastic;

/**
 * limiter
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public final class BlueRateLimiter {

    private final ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    private final Scheduler scheduler;

    public BlueRateLimiter(ReactiveStringRedisTemplate reactiveStringRedisTemplate, Scheduler scheduler) {
        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
        this.scheduler = scheduler != null ? scheduler : boundedElastic();
    }

    private static final Supplier<String> CURRENT_MILLIS_STAMP_SUP = () -> currentTimeMillis() + "";

    private static final RedisScript<Long> SCRIPT = generateScriptByScriptStr(TOKEN_BUCKET_RATE_LIMITER.str, Long.class);

    /**
     * is limit?
     *
     * @param limitKey
     * @param count
     * @param timeoutMillis
     * @return
     */
    public Mono<Boolean> allowed(String limitKey, Long count, Long timeoutMillis) {
        return null;
    }

}
