package com.blue.captcha.remote.provider;


import com.blue.base.constant.base.RandomType;
import com.blue.captcha.api.inter.RpcCaptchaService;
import com.blue.captcha.api.model.CaptchaPair;
import com.blue.captcha.service.inter.CaptchaService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.util.Logger;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import static reactor.util.Loggers.getLogger;

/**
 * rpc secure provider
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl"})
@DubboService(interfaceClass = RpcCaptchaService.class, version = "1.0", methods = {
        @Method(name = "generate", async = true),
        @Method(name = "validate", async = true)
})
public class RpcCaptchaServiceProvider implements RpcCaptchaService {

    private static final Logger LOGGER = getLogger(RpcCaptchaServiceProvider.class);

    private final CaptchaService captchaService;

    public RpcCaptchaServiceProvider(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    /**
     * generate pair
     *
     * @param type
     * @param length
     * @param expire
     * @return
     */
    @Override
    public CompletableFuture<CaptchaPair> generate(RandomType type, int length, Duration expire) {
        LOGGER.info("CompletableFuture<CaptchaPair> generate(RandomType type, int length, Duration expire), type = {}, length = {}, expire = {}", type, length, expire);
        return captchaService.generate(type, length, expire).toFuture();
    }

    /**
     * validate pair
     *
     * @param captchaPair
     * @param repeatable
     * @return
     */
    @Override
    public CompletableFuture<Boolean> validate(CaptchaPair captchaPair, boolean repeatable) {
        LOGGER.info("CompletableFuture<Boolean> validate(CaptchaPair captchaPair, boolean repeatable), captchaPair = {}, repeatable = {}", captchaPair, repeatable);
        return captchaService.validate(captchaPair, repeatable).toFuture();
    }

}
