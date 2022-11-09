package com.blue.auth.handler.api;

import com.blue.auth.service.inter.AuthControlService;
import com.blue.basic.model.common.BlueResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.base.AccessGetter.getAccessReact;
import static com.blue.basic.common.base.AccessGetter.getAuthorizationReact;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.constant.common.BlueHeader.AUTHORIZATION;
import static com.blue.basic.constant.common.BlueHeader.SECRET;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * session api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class SessionApiHandler {

    private final AuthControlService authControlService;

    public SessionApiHandler(AuthControlService authControlService) {
        this.authControlService = authControlService;
    }

    /**
     * session from client
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> insertSession(ServerRequest serverRequest) {
        return authControlService.insertSession(serverRequest);
    }

    /**
     * logout
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> deleteSession(ServerRequest serverRequest) {
        return authControlService.deleteSession(serverRequest);
    }

    /**
     * logout everywhere
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> deleteSessions(ServerRequest serverRequest) {
        return authControlService.deleteSessions(serverRequest);
    }

    /**
     * refresh jwt by refresh token
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> refreshAccess(ServerRequest serverRequest) {
        return getAuthorizationReact(serverRequest)
                .flatMap(authControlService::refreshAccessByRefresh)
                .flatMap(ma ->
                        ok().contentType(APPLICATION_JSON)
                                .header(AUTHORIZATION.name, ma.getAccess())
                                .header(SECRET.name, ma.getSecKey())
                                .body(success(serverRequest)
                                        , BlueResponse.class));
    }

    /**
     * refresh member's private key(client) and member's public key(server->redis)
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> refreshSecret(ServerRequest serverRequest) {
        return getAccessReact(serverRequest)
                .flatMap(acc ->
                        authControlService.refreshSecKeyByAccess(acc)
                                .flatMap(secKey ->
                                        ok().contentType(APPLICATION_JSON)
                                                .header(SECRET.name, secKey)
                                                .body(success(serverRequest)
                                                        , BlueResponse.class)));
    }

}