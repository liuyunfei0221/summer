package com.blue.redis.common;

import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.blue.base.common.base.CommonFunctions.LIMIT_KEYS_GENERATOR;
import static com.blue.redis.api.generator.BlueRedisScriptGenerator.generateScriptByScriptStr;
import static com.blue.redis.constant.RedisScripts.TOKEN_BUCKET_RATE_LIMITER;
import static java.time.Instant.now;
import static java.util.Arrays.asList;
import static reactor.core.scheduler.Schedulers.boundedElastic;

/**
 * limiter
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public final class BlueTokenBucketRateLimiter {

    private final ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    private final Scheduler scheduler;

    private String replenishRate, burstCapacity;

    public BlueTokenBucketRateLimiter(ReactiveStringRedisTemplate reactiveStringRedisTemplate, Scheduler scheduler, String replenishRate, String burstCapacity) {
        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
        this.scheduler = scheduler != null ? scheduler : boundedElastic();
        this.replenishRate = replenishRate;
        this.burstCapacity = burstCapacity;
    }

    private static final Supplier<String> CURRENT_SEC_STAMP_SUP = () -> now().getEpochSecond() + "";

    private static final RedisScript<Long> SCRIPT = generateScriptByScriptStr(TOKEN_BUCKET_RATE_LIMITER.str, Long.class);

    private final Supplier<List<String>> SCRIPT_ARGS_SUP = () ->
            asList(replenishRate, burstCapacity, CURRENT_SEC_STAMP_SUP.get());

    private static final Function<Throwable, Flux<Long>> FALL_BACKER = e ->
            Flux.just(1L);

    /**
     * is a limit key?
     *
     * @param limitKey
     * @return
     */
    public Mono<Boolean> allowed(String limitKey) {
        return reactiveStringRedisTemplate.execute(SCRIPT, LIMIT_KEYS_GENERATOR.apply(limitKey),
                        SCRIPT_ARGS_SUP.get())
                .onErrorResume(FALL_BACKER)
                .elementAt(0)
                .flatMap(allowed ->
                        Mono.just(allowed == 1L))
                .publishOn(scheduler);
    }

}
