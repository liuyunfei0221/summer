package com.blue.verify.service.inter;

import com.blue.base.constant.verify.VerifyType;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * verify service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface VerifyService {

    /**
     * generate verify
     *
     * @param type
     * @return
     */
    Mono<String> generate(VerifyType type);

    /**
     * generate verify
     *
     * @param type
     * @param key
     * @return
     */
    Mono<String> generate(VerifyType type, String key);

    /**
     * generate verify
     *
     * @param type
     * @param key
     * @param length
     * @return
     */
    Mono<String> generate(VerifyType type, String key, Integer length);

    /**
     * generate verify
     *
     * @param type
     * @param key
     * @param length
     * @param expire
     * @return
     */
    Mono<String> generate(VerifyType type, String key, Integer length, Duration expire);

    /**
     * validate verify
     *
     * @param type
     * @param key
     * @param verify
     * @return
     */
    Mono<Boolean> validate(VerifyType type, String key, String verify);

    /**
     * validate verify
     *
     * @param type
     * @param key
     * @param verify
     * @param repeatable
     * @return
     */
    Mono<Boolean> validate(VerifyType type, String key, String verify, Boolean repeatable);

}
