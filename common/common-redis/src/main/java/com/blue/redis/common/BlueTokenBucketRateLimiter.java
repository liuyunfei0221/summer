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

import static com.blue.base.common.base.BlueChecker.isNotNull;
import static com.blue.base.common.base.BlueChecker.isNull;
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
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "unused"})
public final class BlueTokenBucketRateLimiter {

    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    private Scheduler scheduler;

    private String replenishRate, burstCapacity;

    public BlueTokenBucketRateLimiter(ReactiveStringRedisTemplate reactiveStringRedisTemplate, Scheduler scheduler, Integer replenishRate, Integer burstCapacity) {
        assertParam(reactiveStringRedisTemplate, replenishRate, burstCapacity);

        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
        this.scheduler = isNotNull(scheduler) ? scheduler : boundedElastic();

        this.replenishRate = valueOf(replenishRate);
        this.burstCapacity = valueOf(burstCapacity);
    }

    private static final Supplier<String> CURRENT_SEC_STAMP_SUP = () -> now().getEpochSecond() + "";

    private static final RedisScript<Boolean> SCRIPT = generateScriptByScriptStr(TOKEN_BUCKET_RATE_LIMITER.str, Boolean.class);

    private static final String KEY_PREFIX = "tb_rli_";
    private static final String TOKEN_SUFFIX = "_tks", STAMP_SUFFIX = "_tst";

    private static final Function<String, List<String>> SCRIPT_KEYS_WRAPPER = id -> {
        String prefix = KEY_PREFIX + id;
        return asList(prefix + TOKEN_SUFFIX, prefix + STAMP_SUFFIX);
    };

    private final Supplier<List<String>> SCRIPT_ARGS_SUP = () ->
            asList(replenishRate, burstCapacity, CURRENT_SEC_STAMP_SUP.get());

    private static final Function<Throwable, Flux<Boolean>> FALL_BACKER = e ->
            Flux.just(true);

    private final Function<String, Mono<Boolean>> ALLOWED_GETTER = limitKey ->
            reactiveStringRedisTemplate.execute(SCRIPT, SCRIPT_KEYS_WRAPPER.apply(limitKey),
                            SCRIPT_ARGS_SUP.get())
                    .onErrorResume(FALL_BACKER)
                    .elementAt(0)
                    .subscribeOn(scheduler);

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
     * key allowed?
     *
     * @param limitKey
     * @return
     */
    public Boolean isAllowedBySync(String limitKey) {
        return ALLOWED_GETTER.apply(limitKey).toFuture().join();
    }

    /**
     * assert params
     *
     * @param reactiveStringRedisTemplate
     * @param replenishRate
     * @param burstCapacity
     */
    private void assertParam(ReactiveStringRedisTemplate reactiveStringRedisTemplate, Integer replenishRate, Integer burstCapacity) {
        if (isNull(reactiveStringRedisTemplate))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "reactiveStringRedisTemplate can't be null");

        if (isNull(replenishRate) || isNull(burstCapacity) || replenishRate < 1 || burstCapacity < replenishRate)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "replenishRate and burstCapacity can't be null or less than 1, burstCapacity can't be less than replenishRate");
    }

}
