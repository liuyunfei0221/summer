package com.blue.redis.api.generator;

import com.blue.redis.common.BlueValueMarker;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * value marker generator
 *
 * @author blue
 */
@SuppressWarnings("JavaDoc")
public final class BlueValueMarkerGenerator {

    /**
     * generate value marker
     *
     * @param stringRedisTemplate
     * @return
     */
    public static BlueValueMarker generateValueMarker(StringRedisTemplate stringRedisTemplate) {
        return new BlueValueMarker(stringRedisTemplate);
    }

}
