package com.blue.redis.api.generator;

import com.blue.redis.component.BlueFixedTokenBucketRateLimiter;
import com.blue.redis.component.BlueLeakyBucketRateLimiter;
import com.blue.redis.component.BlueTokenBucketRateLimiter;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;

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
        return new BlueFixedTokenBucketRateLimiter(reactiveStringRedisTemplate, replenishRate, burstCapacity);
    }

    /**
     * generate token bucket rate limiter
     *
     * @param reactiveStringRedisTemplate
     * @return
     */
    public static BlueTokenBucketRateLimiter generateTokenBucketRateLimiter(ReactiveStringRedisTemplate reactiveStringRedisTemplate) {
        return new BlueTokenBucketRateLimiter(reactiveStringRedisTemplate);
    }

    /**
     * generate leaky bucket rate limiter
     *
     * @param reactiveStringRedisTemplate
     * @return
     */
    public static BlueLeakyBucketRateLimiter generateLeakyBucketRateLimiter(ReactiveStringRedisTemplate reactiveStringRedisTemplate) {
        return new BlueLeakyBucketRateLimiter(reactiveStringRedisTemplate);
    }

}
