package com.blue.secure.handler.api;

import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.secure.api.model.ClientLoginParam;
import com.blue.secure.service.inter.SecureService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.BlueHeader.AUTHORIZATION;
import static com.blue.base.constant.base.BlueHeader.SECRET;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;

/**
 * secure api handler
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
@Component
public final class SecureApiHandler {

    private final SecureService secureService;

    public SecureApiHandler(SecureService secureService) {
        this.secureService = secureService;
    }

    /**
     * login from client
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> loginByClient(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ClientLoginParam.class)
                .switchIfEmpty(error(new BlueException(EMPTY_PARAM)))
                .flatMap(secureService::loginByClient)
                .flatMap(ma ->
                        ok().contentType(APPLICATION_JSON)
                                .header(AUTHORIZATION.name, ma.getAuth())
                                .header(SECRET.name, ma.getSecKey())
                                .body(generate(OK.code, OK.message, OK.message)
                                        , BlueResponse.class));
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
                                                .body(generate(OK.code, OK.message, OK.message)
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
                                                .body(generate(OK.code, authority, OK.message)
                                                        , BlueResponse.class)));
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
                                                        generate(OK.code, OK.message, OK.message)
                                                        , BlueResponse.class)));
    }

}
