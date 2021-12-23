package com.blue.captcha.api.inter;

import com.blue.base.constant.base.RandomType;
import com.blue.captcha.api.model.CaptchaPair;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

/**
 * rpc captcha interface
 *
 * @author DarkBlue
 * @date 2021/8/12
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
public interface RpcCaptchaService {

    /**
     * generate pair
     *
     * @param type
     * @param length
     * @param expire
     * @return
     */
    CompletableFuture<CaptchaPair> generate(RandomType type, int length, Duration expire);

    /**
     * validate pair
     *
     * @param captchaPair
     * @param repeatable
     * @return
     */
    CompletableFuture<Boolean> validate(CaptchaPair captchaPair, boolean repeatable);

}
