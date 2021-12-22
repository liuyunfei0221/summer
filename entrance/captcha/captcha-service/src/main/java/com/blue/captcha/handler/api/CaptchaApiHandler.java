package com.blue.captcha.handler.api;

import com.blue.captcha.service.inter.CaptchaService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;


/**
 * captcha api handler
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "Duplicates"})
@Component
public final class CaptchaApiHandler {

    private final CaptchaService captchaService;

    public CaptchaApiHandler(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    /**
     * download
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> generate(ServerRequest serverRequest) {
        return captchaService.generate(serverRequest);
    }

}
