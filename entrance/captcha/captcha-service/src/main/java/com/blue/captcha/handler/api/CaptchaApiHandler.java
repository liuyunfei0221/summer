package com.blue.captcha.handler.api;

import com.blue.captcha.service.inter.CaptchaHandleService;
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

    private final CaptchaHandleService captchaHandleService;

    public CaptchaApiHandler(CaptchaHandleService captchaHandleService) {
        this.captchaHandleService = captchaHandleService;
    }

    /**
     * download
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> generate(ServerRequest serverRequest) {
        return captchaHandleService.generate(serverRequest);
    }

}
