package com.blue.redis.component;

import com.blue.basic.model.exps.BlueException;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.blue.basic.common.base.BlueChecker.isNotBlank;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static com.blue.redis.api.generator.BlueRedisScriptGenerator.generateScriptByScriptStr;
import static com.blue.redis.constant.RedisScripts.TOKEN_BUCKET_RATE_LIMITER;
import static java.lang.String.valueOf;
import static java.time.Instant.now;
import static java.util.Arrays.asList;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;

/**
 * token bucket rate limiter
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "unused"})
public final class BlueFixedTokenBucketRateLimiter {

    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    private String replenishRate, burstCapacity;

    public BlueFixedTokenBucketRateLimiter(ReactiveStringRedisTemplate reactiveStringRedisTemplate, Integer replenishRate, Integer burstCapacity) {
        assertParam(reactiveStringRedisTemplate, replenishRate, burstCapacity);

        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
        this.replenishRate = valueOf(replenishRate);
        this.burstCapacity = valueOf(burstCapacity);
    }

    private static final Supplier<String> CURRENT_SEC_STAMP_SUP = () -> now().getEpochSecond() + EMPTY_VALUE.value;

    private static final RedisScript<Boolean> SCRIPT = generateScriptByScriptStr(TOKEN_BUCKET_RATE_LIMITER.str, Boolean.class);

    private static final String KEY_PREFIX = "TB_RLI_";
    private static final String TOKEN_SUFFIX = "_TKS", STAMP_SUFFIX = "_TST";

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
                    .elementAt(0);

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
     * @param reactiveStringRedisTemplate
     * @param replenishRate
     * @param burstCapacity
     */
    private void assertParam(ReactiveStringRedisTemplate reactiveStringRedisTemplate, Integer replenishRate, Integer burstCapacity) {
        if (isNull(reactiveStringRedisTemplate))
            throw new BlueException(INVALID_PARAM);

        if (isNull(replenishRate) || isNull(burstCapacity) || replenishRate < 1 || burstCapacity < replenishRate)
            throw new BlueException(INVALID_PARAM);
    }

}
