package com.blue.secure.handler.api;

import com.blue.base.model.base.BlueResponse;
import com.blue.secure.service.inter.LoginService;
import com.blue.secure.service.inter.SecureService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.BlueHeader.AUTHORIZATION;
import static com.blue.base.constant.base.BlueHeader.SECRET;
import static com.blue.base.constant.base.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * secure api handler
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
@Component
public final class AuthApiHandler {

    private final SecureService secureService;

    private final LoginService loginService;

    public AuthApiHandler(SecureService secureService, LoginService loginService) {
        this.secureService = secureService;
        this.loginService = loginService;
    }

    /**
     * login from client
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        return loginService.login(serverRequest);
    }

    /**
     * logout
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> logout(ServerRequest serverRequest) {
        return getAccessReact(serverRequest)
                .flatMap(acc ->
                        secureService.invalidAuthByAccess(acc)
                                .flatMap(success ->
                                        ok().contentType(APPLICATION_JSON)
                                                .header(AUTHORIZATION.name, "")
                                                .body(
                                                        generate(OK.code, serverRequest)
                                                        , BlueResponse.class)));
    }

    /**
     * update member's private key(client) and member's public key(server->redis)
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> updateSecret(ServerRequest serverRequest) {
        return getAccessReact(serverRequest)
                .flatMap(acc ->
                        secureService.updateSecKeyByAccess(acc)
                                .flatMap(secKey ->
                                        ok().contentType(APPLICATION_JSON)
                                                .header(SECRET.name, secKey)
                                                .body(generate(OK.code, serverRequest)
                                                        , BlueResponse.class)));
    }

    /**
     * get authority info
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> selectAuthority(ServerRequest serverRequest) {
        return getAccessReact(serverRequest)
                .flatMap(acc ->
                        secureService.getAuthorityMonoByAccess(acc)
                                .flatMap(authority ->
                                        ok().contentType(APPLICATION_JSON)
                                                .body(generate(OK.code, authority, serverRequest)
                                                        , BlueResponse.class)));
    }

}
