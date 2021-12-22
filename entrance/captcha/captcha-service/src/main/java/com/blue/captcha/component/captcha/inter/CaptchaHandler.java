package com.blue.captcha.component.captcha.inter;

import com.blue.base.constant.captcha.CaptchaType;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * captcha handler interface
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface CaptchaHandler {

    /**
     * handle captcha
     *
     * @param destination
     */
    Mono<ServerResponse> handle(String destination);

    /**
     * target captcha type to process
     *
     * @return
     */
    CaptchaType targetType();

}
