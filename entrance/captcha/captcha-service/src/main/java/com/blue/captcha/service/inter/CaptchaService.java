package com.blue.captcha.service.inter;

import com.blue.base.constant.base.RandomType;
import com.blue.captcha.api.model.CaptchaPair;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * captcha service
 *
 * @author liuyunfei
 * @date 2021/12/23
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
public interface CaptchaService {

    /**
     * generate pair
     *
     * @param type
     * @param length
     * @param expire
     * @return
     */
    Mono<CaptchaPair> generate(RandomType type, int length, Duration expire);

    /**
     * validate pair
     *
     * @param captchaPair
     * @param repeatable
     * @return
     */
    Mono<Boolean> validate(CaptchaPair captchaPair,boolean repeatable);

}
