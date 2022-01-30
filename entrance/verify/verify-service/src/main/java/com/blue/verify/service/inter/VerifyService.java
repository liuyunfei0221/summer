package com.blue.verify.service.inter;

import com.blue.base.constant.verify.VerifyType;
import com.blue.verify.api.model.VerifyPair;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * verify service
 *
 * @author liuyunfei
 * @date 2021/12/23
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface VerifyService {

    /**
     * generate pair
     *
     * @param type
     * @return
     */
    Mono<VerifyPair> generate(VerifyType type);

    /**
     * generate pair
     *
     * @param type
     * @param key
     * @return
     */
    Mono<VerifyPair> generate(VerifyType type, String key);

    /**
     * generate pair
     *
     * @param type
     * @param key
     * @param length
     * @return
     */
    Mono<VerifyPair> generate(VerifyType type, String key, Integer length);

    /**
     * generate pair
     *
     * @param type
     * @param key
     * @param length
     * @param expire
     * @return
     */
    Mono<VerifyPair> generate(VerifyType type, String key, Integer length, Duration expire);

    /**
     * validate pair
     *
     * @param type
     * @param verifyPair
     * @return
     */
    Mono<Boolean> validate(VerifyType type, VerifyPair verifyPair);

    /**
     * validate pair
     *
     * @param type
     * @param verifyPair
     * @param repeatable
     * @return
     */
    Mono<Boolean> validate(VerifyType type, VerifyPair verifyPair, Boolean repeatable);

}
