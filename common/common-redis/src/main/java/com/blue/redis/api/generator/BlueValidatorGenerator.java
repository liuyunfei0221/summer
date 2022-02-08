package com.blue.redis.api.generator;

import com.blue.redis.common.BlueValidator;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import reactor.core.scheduler.Scheduler;

import static reactor.core.scheduler.Schedulers.boundedElastic;

/**
 * validator generator
 *
 * @author blue
 */
@SuppressWarnings("JavaDoc")
public final class BlueValidatorGenerator {

    /**
     * generate validator
     *
     * @param reactiveStringRedisTemplate
     * @return
     */
    public static BlueValidator generateValidator(ReactiveStringRedisTemplate reactiveStringRedisTemplate) {
        return new BlueValidator(reactiveStringRedisTemplate, boundedElastic());
    }

    /**
     * generate validator
     *
     * @param reactiveStringRedisTemplate
     * @param scheduler
     * @return
     */
    public static BlueValidator generateValidator(ReactiveStringRedisTemplate reactiveStringRedisTemplate, Scheduler scheduler) {
        return new BlueValidator(reactiveStringRedisTemplate, scheduler);
    }

}
