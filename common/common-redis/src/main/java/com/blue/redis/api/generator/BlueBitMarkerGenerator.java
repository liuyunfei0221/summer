package com.blue.redis.api.generator;

import com.blue.redis.component.BlueBitMarker;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;

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
        return new BlueBitMarker(reactiveStringRedisTemplate);
    }

}
