package com.blue.secure.service.impl;

import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.secure.component.login.inter.LoginHandler;
import com.blue.secure.model.LoginParam;
import com.blue.secure.service.inter.LoginService;
import com.blue.secure.service.inter.SecureService;
import com.blue.verify.api.model.VerifyParam;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Function;

import static com.blue.base.common.base.BlueCheck.isEmpty;
import static com.blue.base.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.BlueHeader.AUTHORIZATION;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.verify.VerifyType.IMAGE;
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

    private final SecureService secureService;

    public LoginServiceImpl(SecureService secureService) {
        this.secureService = secureService;
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

    private static final VerifyParam DEFAULT_PARAM = new VerifyParam(IMAGE.identity, "");

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
                        secureService.invalidAuthByAccess(acc)
                                .flatMap(success ->
                                        ok().contentType(APPLICATION_JSON)
                                                .header(AUTHORIZATION.name, "")
                                                .body(
                                                        generate(OK.code, serverRequest)
                                                        , BlueResponse.class)));
    }

}
