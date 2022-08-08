package com.blue.auth.service.inter;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * session service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "UnusedReturnValue"})
public interface SessionService {

    /**
     * session
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
