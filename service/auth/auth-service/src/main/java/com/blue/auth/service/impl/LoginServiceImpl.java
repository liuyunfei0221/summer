package com.blue.auth.service.impl;

import com.blue.auth.component.login.LoginProcessor;
import com.blue.auth.service.inter.AuthService;
import com.blue.auth.service.inter.LoginService;
import com.blue.base.model.base.BlueResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.BlueHeader.AUTHORIZATION;
import static com.blue.base.constant.base.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * login service impl
 *
 * @author DarkBlue
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
    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        return loginProcessor.login(serverRequest);
    }

    /**
     * logout
     *
     * @param serverRequest
     * @return
     */
    @Override
    public Mono<ServerResponse> logout(ServerRequest serverRequest) {
        return getAccessReact(serverRequest)
                .flatMap(acc ->
                        authService.invalidAuthByAccess(acc)
                                .flatMap(success ->
                                        ok().contentType(APPLICATION_JSON)
                                                .header(AUTHORIZATION.name, "")
                                                .body(
                                                        generate(OK.code, serverRequest)
                                                        , BlueResponse.class)));
    }

}
