package com.blue.redis.api.generator;

import com.blue.redis.component.BlueValueMarker;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * value marker generator
 *
 * @author liuyunfei
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
