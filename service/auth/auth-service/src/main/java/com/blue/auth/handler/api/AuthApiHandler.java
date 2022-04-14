package com.blue.auth.handler.api;

import com.blue.auth.model.AccessResetParam;
import com.blue.auth.model.AccessUpdateParam;
import com.blue.auth.model.CredentialSettingUpParam;
import com.blue.auth.service.inter.ControlService;
import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.exps.BlueException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.base.common.reactive.AccessGetterForReactive.getAuthorizationReact;
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
     * refresh jwt by refresh token
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> refreshAccess(ServerRequest serverRequest) {
        return getAuthorizationReact(serverRequest)
                .flatMap(controlService::refreshAccess)
                .flatMap(ma ->
                        ok().contentType(APPLICATION_JSON)
                                .header(AUTHORIZATION.name, ma.getAuth())
                                .header(SECRET.name, ma.getSecKey())
                                .body(generate(OK.code, serverRequest)
                                        , BlueResponse.class));
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
                        controlService.invalidateAuthByAccess(acc)
                                .flatMap(success ->
                                        ok().contentType(APPLICATION_JSON)
                                                .header(AUTHORIZATION.name, "")
                                                .body(generate(OK.code, serverRequest)
                                                        , BlueResponse.class)));
    }

    /**
     * logout everywhere
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> logoutEverywhere(ServerRequest serverRequest) {
        return getAccessReact(serverRequest)
                .flatMap(acc ->
                        controlService.invalidateAuthByMemberId(acc.getId())
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
                .flatMap(r ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, r, serverRequest), BlueResponse.class));
    }

    /**
     * reset member's access/password
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> resetAccess(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(AccessResetParam.class)
                .switchIfEmpty(error(() -> new BlueException(EMPTY_PARAM)))
                .flatMap(controlService::resetAccessByAccess)
                .flatMap(r ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, r, serverRequest), BlueResponse.class));
    }

    /**
     * add new credential base on verify type for a member
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> credentialSettingUp(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(CredentialSettingUpParam.class)
                        .switchIfEmpty(error(() -> new BlueException(EMPTY_PARAM))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 ->
                        controlService.credentialSettingUp(tuple2.getT1(), tuple2.getT2()))
                .flatMap(r ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, r, serverRequest), BlueResponse.class));
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
