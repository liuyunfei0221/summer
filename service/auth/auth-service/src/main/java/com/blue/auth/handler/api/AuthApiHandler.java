package com.blue.auth.handler.api;

import com.blue.auth.model.*;
import com.blue.auth.service.inter.AuthControlService;
import com.blue.base.model.common.BlueResponse;
import com.blue.base.model.exps.BlueException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.auth.constant.AuthTypeReference.LIST_PARAM_FOR_QUESTION_INSERT_PARAM_TYPE;
import static com.blue.base.common.reactive.AccessGetterForReactive.*;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.common.BlueHeader.AUTHORIZATION;
import static com.blue.base.constant.common.BlueHeader.SECRET;
import static com.blue.base.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.common.ResponseElement.OK;
import static com.blue.base.constant.common.SpecialStringElement.EMPTY_DATA;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * auth api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class AuthApiHandler {

    private final AuthControlService authControlService;

    public AuthApiHandler(AuthControlService authControlService) {
        this.authControlService = authControlService;
    }

    /**
     * login from client
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        return authControlService.login(serverRequest);
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
                                .header(AUTHORIZATION.name, ma.getAuth())
                                .header(SECRET.name, ma.getSecKey())
                                .body(generate(OK.code, serverRequest)
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
                                                .body(generate(OK.code, serverRequest)
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
                        authControlService.invalidateAuthByAccess(acc)
                                .flatMap(success ->
                                        ok().contentType(APPLICATION_JSON)
                                                .header(AUTHORIZATION.name, EMPTY_DATA.value)
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
                        authControlService.invalidateAuthByMemberId(acc.getId())
                                .flatMap(success ->
                                        ok().contentType(APPLICATION_JSON)
                                                .header(AUTHORIZATION.name, EMPTY_DATA.value)
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
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 ->
                        authControlService.updateAccessByAccess(tuple2.getT1(), tuple2.getT2()))
                .flatMap(ig ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, serverRequest), BlueResponse.class));
    }

    /**
     * reset member's access/password
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> resetAccess(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(AccessResetParam.class)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(authControlService::resetAccessByAccess)
                .flatMap(ig ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, serverRequest), BlueResponse.class));
    }

    /**
     * add new credential base on verify type for a member
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> credentialSettingUp(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(CredentialSettingUpParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 ->
                        just(authControlService.credentialSettingUp(tuple2.getT1(), tuple2.getT2())))
                .flatMap(mbi ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, mbi, serverRequest), BlueResponse.class));
    }

    /**
     * update credential
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> credentialModify(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(CredentialModifyParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 ->
                        just(authControlService.credentialModify(tuple2.getT1(), tuple2.getT2())))
                .flatMap(mbi ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, mbi, serverRequest), BlueResponse.class));
    }

    /**
     * insert security question
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> insertSecurityQuestion(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(SecurityQuestionInsertParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> authControlService.insertSecurityQuestion(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(ig ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, serverRequest), BlueResponse.class));
    }

    /**
     * insert security questions
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> insertSecurityQuestions(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(LIST_PARAM_FOR_QUESTION_INSERT_PARAM_TYPE)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> authControlService.insertSecurityQuestions(tuple2.getT1().getData(), tuple2.getT2().getId()))
                .flatMap(ig ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, serverRequest), BlueResponse.class));
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
                        authControlService.selectAuthoritiesMonoByAccess(acc)
                                .flatMap(authority ->
                                        ok().contentType(APPLICATION_JSON)
                                                .body(generate(OK.code, authority, serverRequest)
                                                        , BlueResponse.class)));
    }

}