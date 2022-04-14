package com.blue.auth.service.inter;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * login service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "UnusedReturnValue"})
public interface LoginService {

    /**
     * login
     *
     * @param serverRequest
     * @return
     */
    Mono<ServerResponse> login(ServerRequest serverRequest);

    /**
     * logout
     *
     * @param serverRequest
     * @return
     */
    Mono<ServerResponse> logout(ServerRequest serverRequest);

}
