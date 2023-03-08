package com.blue.auth.handler.api;

import com.blue.auth.service.inter.AuthControlService;
import com.blue.basic.model.common.BlueResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.access.AccessGetter.getAccessReact;
import static com.blue.basic.common.base.CommonFunctions.success;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * authority api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class AuthorityApiHandler {

    private final AuthControlService authControlService;

    public AuthorityApiHandler(AuthControlService authControlService) {
        this.authControlService = authControlService;
    }

    /**
     * get authority info
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> selectAuthorities(ServerRequest serverRequest) {
        return getAccessReact(serverRequest)
                .flatMap(acc ->
                        authControlService.selectAuthoritiesByAccess(acc)
                                .flatMap(authorities ->
                                        ok().contentType(APPLICATION_JSON)
                                                .body(success(authorities, serverRequest)
                                                        , BlueResponse.class)));
    }

    /**
     * get authority info
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> getAuthority(ServerRequest serverRequest) {
        return getAccessReact(serverRequest)
                .flatMap(acc ->
                        authControlService.getAuthorityByAccess(acc)
                                .flatMap(authority ->
                                        ok().contentType(APPLICATION_JSON)
                                                .body(success(authority, serverRequest)
                                                        , BlueResponse.class)));
    }

}