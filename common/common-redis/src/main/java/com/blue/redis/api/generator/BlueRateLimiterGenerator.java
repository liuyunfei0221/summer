package com.blue.redis.api.generator;

import com.blue.redis.component.BlueFixedTokenBucketRateLimiter;
import com.blue.redis.component.BlueLeakyBucketRateLimiter;
import com.blue.redis.component.BlueTokenBucketRateLimiter;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import reactor.core.scheduler.Scheduler;

import static reactor.core.scheduler.Schedulers.boundedElastic;

/**
 * rate limiter generator
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public final class BlueRateLimiterGenerator {

    /**
     * generate token bucket rate limiter
     *
     * @param reactiveStringRedisTemplate
     * @param replenishRate
     * @param burstCapacity
     * @return
     */
    public static BlueFixedTokenBucketRateLimiter generateFixedTokenBucketRateLimiter(ReactiveStringRedisTemplate reactiveStringRedisTemplate, Integer replenishRate, Integer burstCapacity) {
        return new BlueFixedTokenBucketRateLimiter(reactiveStringRedisTemplate, boundedElastic(), replenishRate, burstCapacity);
    }

    /**
     * generate fixed token bucket rate limiter
     *
     * @param reactiveStringRedisTemplate
     * @param scheduler
     * @param replenishRate
     * @param burstCapacity
     * @return
     */
    public static BlueFixedTokenBucketRateLimiter generateFixedTokenBucketRateLimiter(ReactiveStringRedisTemplate reactiveStringRedisTemplate, Scheduler scheduler, Integer replenishRate, Integer burstCapacity) {
        return new BlueFixedTokenBucketRateLimiter(reactiveStringRedisTemplate, scheduler, replenishRate, burstCapacity);
    }

    /**
     * generate token bucket rate limiter
     *
     * @param reactiveStringRedisTemplate
     * @return
     */
    public static BlueTokenBucketRateLimiter generateTokenBucketRateLimiter(ReactiveStringRedisTemplate reactiveStringRedisTemplate) {
        return new BlueTokenBucketRateLimiter(reactiveStringRedisTemplate, boundedElastic());
    }

    /**
     * generate token bucket rate limiter
     *
     * @param reactiveStringRedisTemplate
     * @param scheduler
     * @return
     */
    public static BlueTokenBucketRateLimiter generateTokenBucketRateLimiter(ReactiveStringRedisTemplate reactiveStringRedisTemplate, Scheduler scheduler) {
        return new BlueTokenBucketRateLimiter(reactiveStringRedisTemplate, scheduler);
    }

    /**
     * generate leaky bucket rate limiter
     *
     * @param reactiveStringRedisTemplate
     * @return
     */
    public static BlueLeakyBucketRateLimiter generateLeakyBucketRateLimiter(ReactiveStringRedisTemplate reactiveStringRedisTemplate) {
        return new BlueLeakyBucketRateLimiter(reactiveStringRedisTemplate, boundedElastic());
    }

    /**
     * generate leaky bucket rate limiter
     *
     * @param reactiveStringRedisTemplate
     * @param scheduler
     * @return
     */
    public static BlueLeakyBucketRateLimiter generateLeakyBucketRateLimiter(ReactiveStringRedisTemplate reactiveStringRedisTemplate, Scheduler scheduler) {
        return new BlueLeakyBucketRateLimiter(reactiveStringRedisTemplate, scheduler);
    }

}
