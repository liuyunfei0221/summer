package com.blue.captcha.component.captcha.impl;

import com.blue.base.constant.captcha.CaptchaType;
import com.blue.captcha.common.CaptchaProcessor;
import com.blue.captcha.component.captcha.inter.CaptchaHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import static com.blue.base.constant.captcha.CaptchaType.IMAGE;
import static reactor.util.Loggers.getLogger;

/**
 * @author liuyunfei
 * @date 2021/12/23
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class ImageCaptchaHandler implements CaptchaHandler {

    private static final Logger LOGGER = getLogger(ImageCaptchaHandler.class);

    private final ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    private final CaptchaProcessor captchaProcessor;

    public ImageCaptchaHandler(ReactiveStringRedisTemplate reactiveStringRedisTemplate, CaptchaProcessor captchaProcessor) {
        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
        this.captchaProcessor = captchaProcessor;
    }

    /**
     * generate an image captcha
     *
     * @param destination
     * @return
     */
    @Override
    public Mono<ServerResponse> handle(String destination) {


        return null;
    }

    @Override
    public CaptchaType targetType() {
        return IMAGE;
    }
}
