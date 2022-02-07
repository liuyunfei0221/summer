package com.blue.redis.common;

import com.blue.base.model.exps.BlueException;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.blue.base.common.base.CommonFunctions.LIMIT_KEYS_GENERATOR;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.redis.api.generator.BlueRedisScriptGenerator.generateScriptByScriptStr;
import static com.blue.redis.constant.RedisScripts.TOKEN_BUCKET_RATE_LIMITER;
import static java.lang.String.valueOf;
import static java.time.Instant.now;
import static java.util.Arrays.asList;
import static reactor.core.scheduler.Schedulers.boundedElastic;

/**
 * token bucket rate limiter
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
public final class BlueTokenBucketRateLimiter {

    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    private Scheduler scheduler;

    private String replenishRate, burstCapacity;

    public BlueTokenBucketRateLimiter(ReactiveStringRedisTemplate reactiveStringRedisTemplate, Scheduler scheduler, Integer replenishRate, Integer burstCapacity) {
        assertParam(reactiveStringRedisTemplate, replenishRate, burstCapacity);

        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
        this.scheduler = scheduler != null ? scheduler : boundedElastic();

        this.replenishRate = valueOf(replenishRate);
        this.burstCapacity = valueOf(burstCapacity);
    }

    private static final Supplier<String> CURRENT_SEC_STAMP_SUP = () -> now().getEpochSecond() + "";

    private static final RedisScript<Boolean> SCRIPT = generateScriptByScriptStr(TOKEN_BUCKET_RATE_LIMITER.str, Boolean.class);

    private final Supplier<List<String>> SCRIPT_ARGS_SUP = () ->
            asList(replenishRate, burstCapacity, CURRENT_SEC_STAMP_SUP.get());

    private static final Function<Throwable, Flux<Boolean>> FALL_BACKER = e ->
            Flux.just(true);

    private final Function<String, Mono<Boolean>> ALLOWED_GETTER = limitKey ->
            reactiveStringRedisTemplate.execute(SCRIPT, LIMIT_KEYS_GENERATOR.apply(limitKey),
                            SCRIPT_ARGS_SUP.get())
                    .onErrorResume(FALL_BACKER)
                    .elementAt(0)
                    .publishOn(scheduler);

    /**
     * key allowed?
     *
     * @param limitKey
     * @return
     */
    public Mono<Boolean> isAllowed(String limitKey) {
        return ALLOWED_GETTER.apply(limitKey);
    }

    /**
     * assert params
     *
     * @param reactiveStringRedisTemplate
     * @param replenishRate
     * @param burstCapacity
     */
    private void assertParam(ReactiveStringRedisTemplate reactiveStringRedisTemplate, Integer replenishRate, Integer burstCapacity) {
        if (reactiveStringRedisTemplate == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "reactiveStringRedisTemplate can't be null");

        if (replenishRate == null || burstCapacity == null || replenishRate < 1 || burstCapacity < replenishRate)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "replenishRate and burstCapacity can't be null or less than 1, burstCapacity can't be less than replenishRate");
    }

}
