package com.blue.auth.service.impl;

import com.blue.auth.component.login.inter.LoginHandler;
import com.blue.auth.service.inter.AuthService;
import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.auth.model.LoginParam;
import com.blue.auth.service.inter.LoginService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Function;

import static com.blue.base.common.base.BlueChecker.isEmpty;
import static com.blue.base.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.BlueHeader.AUTHORIZATION;
import static com.blue.base.constant.base.ResponseElement.*;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;

/**
 * login service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class LoginServiceImpl implements LoginService, ApplicationListener<ContextRefreshedEvent> {

    private final AuthService authService;

    public LoginServiceImpl(AuthService authService) {
        this.authService = authService;
    }

    /**
     * login type -> login handler
     */
    private Map<String, LoginHandler> loginHandlers;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        Map<String, LoginHandler> beansOfType = applicationContext.getBeansOfType(LoginHandler.class);
        if (isEmpty(beansOfType))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "loginHandlers is empty");

        loginHandlers = beansOfType.values().stream()
                .collect(toMap(lh -> lh.targetType().identity, lh -> lh, (a, b) -> a));
    }

    private final Function<ServerRequest, Mono<ServerResponse>> loginHandler = serverRequest ->
            serverRequest.bodyToMono(LoginParam.class)
                    .switchIfEmpty(error(() -> new BlueException(EMPTY_PARAM)))
                    .flatMap(lp ->
                            ofNullable(lp.getLoginType())
                                    .map(loginHandlers::get)
                                    .map(h -> h.login(lp, serverRequest))
                                    .orElseThrow(() -> new BlueException(INVALID_PARAM)));

    /**
     * login
     *
     * @param serverRequest
     * @return
     */
    @Override
    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        return loginHandler.apply(serverRequest);
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
