package com.blue.auth.service.impl;

import com.blue.auth.component.login.LoginProcessor;
import com.blue.auth.service.inter.AuthService;
import com.blue.auth.service.inter.LoginService;
import com.blue.base.model.common.BlueResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.AccessGetterForReactive.*;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.common.BlueHeader.AUTHORIZATION;
import static com.blue.base.constant.common.ResponseElement.OK;
import static com.blue.base.constant.common.SpecialStringElement.EMPTY_DATA;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * login service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc"})
@Service
public class LoginServiceImpl implements LoginService {

    private final AuthService authService;

    private final LoginProcessor loginProcessor;

    public LoginServiceImpl(AuthService authService, LoginProcessor loginProcessor) {
        this.authService = authService;
        this.loginProcessor = loginProcessor;
    }

    /**
     * login
     *
     * @param serverRequest
     * @return
     */
    @Override
    public Mono<ServerResponse> insertSession(ServerRequest serverRequest) {
        return loginProcessor.login(serverRequest);
    }

    /**
     * logout
     *
     * @param serverRequest
     * @return
     */
    @Override
    public Mono<ServerResponse> deleteSession(ServerRequest serverRequest) {
        return getAccessReact(serverRequest)
                .flatMap(acc ->
                        authService.invalidateAuthByAccess(acc)
                                .flatMap(success ->
                                        ok().contentType(APPLICATION_JSON)
                                                .header(AUTHORIZATION.name, EMPTY_DATA.value)
                                                .body(
                                                        generate(OK.code, serverRequest)
                                                        , BlueResponse.class)));
    }

    /**
     * logout everywhere
     *
     * @param serverRequest
     * @return
     */
    @Override
    public Mono<ServerResponse> deleteSessions(ServerRequest serverRequest) {
        return getAccessReact(serverRequest)
                .flatMap(acc ->
                        authService.invalidateAuthByMemberId(acc.getId())
                                .flatMap(success ->
                                        ok().contentType(APPLICATION_JSON)
                                                .header(AUTHORIZATION.name, EMPTY_DATA.value)
                                                .body(generate(OK.code, serverRequest)
                                                        , BlueResponse.class)));
    }

}
