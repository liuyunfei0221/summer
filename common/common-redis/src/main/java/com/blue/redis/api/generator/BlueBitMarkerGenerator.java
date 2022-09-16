package com.blue.redis.api.generator;

import com.blue.redis.component.BlueBitMarker;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import reactor.core.scheduler.Scheduler;

import static reactor.core.scheduler.Schedulers.boundedElastic;

/**
 * bit marker generator
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public final class BlueBitMarkerGenerator {

    /**
     * generate bit marker
     *
     * @param reactiveStringRedisTemplate
     * @return
     */
    public static BlueBitMarker generateBitMarker(ReactiveStringRedisTemplate reactiveStringRedisTemplate) {
        return new BlueBitMarker(reactiveStringRedisTemplate, boundedElastic());
    }

    /**
     * generate bit marker
     *
     * @param reactiveStringRedisTemplate
     * @param scheduler
     * @return
     */
    public static BlueBitMarker generateBitMarker(ReactiveStringRedisTemplate reactiveStringRedisTemplate, Scheduler scheduler) {
        return new BlueBitMarker(reactiveStringRedisTemplate, scheduler);
    }

}
