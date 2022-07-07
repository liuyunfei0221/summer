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
    Mono<ServerResponse> insertSession(ServerRequest serverRequest);

    /**
     * logout
     *
     * @param serverRequest
     * @return
     */
    Mono<ServerResponse> deleteSession(ServerRequest serverRequest);

    /**
     * logout everywhere
     *
     * @param serverRequest
     * @return
     */
    Mono<ServerResponse> deleteSessions(ServerRequest serverRequest);

}
