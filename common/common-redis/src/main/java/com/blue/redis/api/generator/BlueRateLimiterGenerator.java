package com.blue.redis.api.generator;

import com.blue.redis.common.BlueLeakyBucketRateLimiter;
import com.blue.redis.common.BlueTokenBucketRateLimiter;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import reactor.core.scheduler.Scheduler;

import static reactor.core.scheduler.Schedulers.boundedElastic;

/**
 * rate limiter generator
 *
 * @author blue
 */
@SuppressWarnings("JavaDoc")
public final class BlueRateLimiterGenerator {

    /**
     * generate token bucket rate limiter
     *
     * @param reactiveStringRedisTemplate
     * @param replenishRate
     * @param burstCapacity
     * @return
     */
    public static BlueTokenBucketRateLimiter generateTokenBucketRateLimiter(ReactiveStringRedisTemplate reactiveStringRedisTemplate, Integer replenishRate, Integer burstCapacity) {
        return new BlueTokenBucketRateLimiter(reactiveStringRedisTemplate, boundedElastic(), replenishRate, burstCapacity);
    }

    /**
     * generate token bucket rate limiter
     *
     * @param reactiveStringRedisTemplate
     * @param scheduler
     * @param replenishRate
     * @param burstCapacity
     * @return
     */
    public static BlueTokenBucketRateLimiter generateTokenBucketRateLimiter(ReactiveStringRedisTemplate reactiveStringRedisTemplate, Scheduler scheduler, Integer replenishRate, Integer burstCapacity) {
        return new BlueTokenBucketRateLimiter(reactiveStringRedisTemplate, scheduler, replenishRate, burstCapacity);
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
