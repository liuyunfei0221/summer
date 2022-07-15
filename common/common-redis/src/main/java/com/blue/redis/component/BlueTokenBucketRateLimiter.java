package com.blue.redis.component;

import com.blue.basic.model.exps.BlueException;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_DATA;
import static com.blue.redis.api.generator.BlueRedisScriptGenerator.generateScriptByScriptStr;
import static com.blue.redis.constant.RedisScripts.TOKEN_BUCKET_RATE_LIMITER;
import static java.lang.String.valueOf;
import static java.time.Instant.now;
import static java.util.Arrays.asList;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static reactor.core.scheduler.Schedulers.boundedElastic;

/**
 * token bucket rate limiter
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "unused"})
public final class BlueTokenBucketRateLimiter {

    private final ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    private final Scheduler scheduler;

    public BlueTokenBucketRateLimiter(ReactiveStringRedisTemplate reactiveStringRedisTemplate, Scheduler scheduler) {
        if (isNull(reactiveStringRedisTemplate))
            throw new RuntimeException("reactiveStringRedisTemplate can't be null");

        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
        this.scheduler = isNotNull(scheduler) ? scheduler : boundedElastic();
    }

    private static final Supplier<String> CURRENT_SEC_STAMP_SUP = () -> now().getEpochSecond() + EMPTY_DATA.value;

    private static final RedisScript<Boolean> SCRIPT = generateScriptByScriptStr(TOKEN_BUCKET_RATE_LIMITER.str, Boolean.class);

    private static final String KEY_PREFIX = "TB_RLI_";
    private static final String TOKEN_SUFFIX = "_TKS", STAMP_SUFFIX = "_TST";

    private static final Function<String, List<String>> SCRIPT_KEYS_WRAPPER = id -> {
        String prefix = KEY_PREFIX + id;
        return asList(prefix + TOKEN_SUFFIX, prefix + STAMP_SUFFIX);
    };

    private final BiFunction<Integer, Integer, List<String>> SCRIPT_ARGS_FUNC = (replenishRate, burstCapacity) ->
            asList(valueOf(replenishRate), valueOf(burstCapacity), CURRENT_SEC_STAMP_SUP.get());

    private static final Function<Throwable, Flux<Boolean>> FALL_BACKER = e ->
            Flux.just(true);

    private Mono<Boolean> allowedValidate(String limitKey, Integer replenishRate, Integer burstCapacity) {
        assertParam(limitKey, replenishRate, burstCapacity);

        return reactiveStringRedisTemplate.execute(SCRIPT, SCRIPT_KEYS_WRAPPER.apply(limitKey),
                        SCRIPT_ARGS_FUNC.apply(replenishRate, burstCapacity))
                .publishOn(scheduler)
                .onErrorResume(FALL_BACKER)
                .elementAt(0);
    }

    /**
     * key allowed?
     *
     * @param limitKey
     * @param replenishRate
     * @param burstCapacity
     * @return
     */
    public Mono<Boolean> isAllowed(String limitKey, Integer replenishRate, Integer burstCapacity) {
        return allowedValidate(limitKey, replenishRate, burstCapacity);
    }

    /**
     * key allowed?
     *
     * @param limitKey
     * @param replenishRate
     * @param burstCapacity
     * @return
     */
    public Boolean isAllowedBySync(String limitKey, Integer replenishRate, Integer burstCapacity) {
        return allowedValidate(limitKey, replenishRate, burstCapacity).toFuture().join();
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
                        .publishOn(scheduler).flatMap(r -> just(r > 0))
                :
                error(() -> new BlueException(BAD_REQUEST));
    }

    /**
     * assert params
     *
     * @param limitKey
     * @param replenishRate
     * @param burstCapacity
     */
    private void assertParam(String limitKey, Integer replenishRate, Integer burstCapacity) {
        if (isBlank(limitKey))
            throw new RuntimeException("limitKey can't be blank");

        if (isNull(replenishRate) || isNull(burstCapacity) || replenishRate < 1 || burstCapacity < replenishRate)
            throw new RuntimeException("replenishRate and burstCapacity can't be null or less than 1, burstCapacity can't be less than replenishRate");
    }

}
