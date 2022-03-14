package com.blue.auth.handler.api;

import com.blue.auth.model.AccessUpdateParam;
import com.blue.auth.service.inter.ControlService;
import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.exps.BlueException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.BlueHeader.AUTHORIZATION;
import static com.blue.base.constant.base.BlueHeader.SECRET;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.base.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.zip;

/**
 * auth api handler
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
@Component
public final class AuthApiHandler {

    private final ControlService controlService;

    public AuthApiHandler(ControlService controlService) {
        this.controlService = controlService;
    }

    /**
     * login from client
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        return controlService.login(serverRequest);
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
                        controlService.invalidAuthByAccess(acc)
                                .flatMap(success ->
                                        ok().contentType(APPLICATION_JSON)
                                                .header(AUTHORIZATION.name, "")
                                                .body(generate(OK.code, serverRequest)
                                                        , BlueResponse.class)));
    }

    /**
     * update member's access/password
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> updateAccess(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(AccessUpdateParam.class)
                        .switchIfEmpty(error(() -> new BlueException(EMPTY_PARAM))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 ->
                        controlService.updateAccessByAccess(tuple2.getT1(), tuple2.getT2()))
                .flatMap(ri ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, ri, serverRequest), BlueResponse.class));
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
                        controlService.updateSecKeyByAccess(acc)
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
                        controlService.getAuthorityMonoByAccess(acc)
                                .flatMap(authority ->
                                        ok().contentType(APPLICATION_JSON)
                                                .body(generate(OK.code, authority, serverRequest)
                                                        , BlueResponse.class)));
    }

}
