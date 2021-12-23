package com.blue.secure.remote.consumer;

import com.blue.base.constant.base.RandomType;
import com.blue.captcha.api.inter.RpcCaptchaService;
import com.blue.captcha.api.model.CaptchaPair;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.time.Duration;

import static reactor.core.publisher.Mono.fromFuture;
import static reactor.util.Loggers.getLogger;

/**
 * @author liuyunfei
 * @date 2021/12/23
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused"})
@Component
public class RpcCaptchaServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcCaptchaServiceConsumer.class);

    @DubboReference(version = "1.0", providedBy = {"summer-captcha"}, methods = {
            @Method(name = "generate", async = true),
            @Method(name = "validate", async = true)
    })
    private RpcCaptchaService rpcCaptchaService;

    /**
     * generate pair
     *
     * @param type
     * @param length
     * @param expire
     * @return
     */
    Mono<CaptchaPair> generate(RandomType type, int length, Duration expire) {
        LOGGER.info("Mono<CaptchaPair> generate(RandomType type, int length, Duration expire), type = {}, length = {}, expire ={}", type, length, expire);
        return fromFuture(rpcCaptchaService.generate(type, length, expire));
    }

    /**
     * validate pair
     *
     * @param captchaPair
     * @param repeatable
     * @return
     */
    Mono<Boolean> validate(CaptchaPair captchaPair, boolean repeatable) {
        LOGGER.info("Mono<Boolean> validate(CaptchaPair captchaPair, boolean repeatable), captchaPair = {}, repeatable = {}", captchaPair, repeatable);
        return fromFuture(rpcCaptchaService.validate(captchaPair, repeatable));
    }

}
