package com.blue.verify.service.inter;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * verify handle service
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface VerifyHandleService {

    /**
     * generate verify
     *
     * @param serverRequest
     * @return
     */
    Mono<ServerResponse> generate(ServerRequest serverRequest);

}
