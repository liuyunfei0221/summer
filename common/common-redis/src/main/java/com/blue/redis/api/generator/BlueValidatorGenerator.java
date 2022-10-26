package com.blue.redis.api.generator;

import com.blue.redis.component.BlueValidator;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;

/**
 * validator generator
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public final class BlueValidatorGenerator {

    /**
     * generate validator
     *
     * @param reactiveStringRedisTemplate
     * @return
     */
    public static BlueValidator generateValidator(ReactiveStringRedisTemplate reactiveStringRedisTemplate) {
        return new BlueValidator(reactiveStringRedisTemplate);
    }

}
