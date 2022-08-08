package com.blue.auth.service.impl;

import com.blue.auth.component.session.SessionProcessor;
import com.blue.auth.service.inter.SessionService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * session service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc"})
@Service
public class SessionServiceImpl implements SessionService {

    private final SessionProcessor sessionProcessor;

    public SessionServiceImpl(SessionProcessor sessionProcessor) {
        this.sessionProcessor = sessionProcessor;
    }

    /**
     * session
     *
     * @param serverRequest
     * @return
     */
    @Override
    public Mono<ServerResponse> insertSession(ServerRequest serverRequest) {
        return sessionProcessor.insertSession(serverRequest);
    }

    /**
     * logout
     *
     * @param serverRequest
     * @return
     */
    @Override
    public Mono<ServerResponse> deleteSession(ServerRequest serverRequest) {
        return sessionProcessor.deleteSession(serverRequest);
    }

    /**
     * logout everywhere
     *
     * @param serverRequest
     * @return
     */
    @Override
    public Mono<ServerResponse> deleteSessions(ServerRequest serverRequest) {
        return sessionProcessor.deleteSessions(serverRequest);
    }

}
