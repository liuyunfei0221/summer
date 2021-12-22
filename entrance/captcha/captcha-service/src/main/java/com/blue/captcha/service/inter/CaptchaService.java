package com.blue.captcha.service.inter;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * captcha service
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface CaptchaService {

    /**
     * generate captcha
     *
     * @param serverRequest
     * @return
     */
    Mono<ServerResponse> generate(ServerRequest serverRequest);

}
