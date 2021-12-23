package com.blue.captcha.service.inter;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * captcha handle service
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface CaptchaHandleService {

    /**
     * generate captcha
     *
     * @param serverRequest
     * @return
     */
    Mono<ServerResponse> generate(ServerRequest serverRequest);

}
