package com.blue.verify.service.inter;

import com.blue.base.constant.base.RandomType;
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
     * @return
     */
    Mono<VerifyPair> generate();

    /**
     * generate pair
     *
     * @param type
     * @return
     */
    Mono<VerifyPair> generate(RandomType type);

    /**
     * generate pair
     *
     * @param type
     * @param length
     * @return
     */
    Mono<VerifyPair> generate(RandomType type, Integer length);

    /**
     * generate pair
     *
     * @param type
     * @param length
     * @param expire
     * @return
     */
    Mono<VerifyPair> generate(RandomType type, Integer length, Duration expire);

    /**
     * validate pair
     *
     * @param verifyPair
     * @return
     */
    Mono<Boolean> validate(VerifyPair verifyPair);

    /**
     * validate pair
     *
     * @param verifyPair
     * @param repeatable
     * @return
     */
    Mono<Boolean> validate(VerifyPair verifyPair, Boolean repeatable);

}
