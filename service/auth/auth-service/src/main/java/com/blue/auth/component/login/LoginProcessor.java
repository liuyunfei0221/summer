package com.blue.auth.component.login;

import com.blue.auth.component.login.inter.LoginHandler;
import com.blue.auth.model.LoginParam;
import com.blue.base.model.exps.BlueException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.function.Function;

import static com.blue.base.common.base.BlueChecker.isEmpty;
import static com.blue.base.constant.base.ResponseElement.*;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static reactor.core.publisher.Mono.error;

/**
 * login processor
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Component
public class LoginProcessor implements ApplicationListener<ContextRefreshedEvent> {

    /**
     * credential type -> login handler
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
                            ofNullable(lp.getCredentialType())
                                    .map(loginHandlers::get)
                                    .map(h -> h.login(lp, serverRequest))
                                    .orElseThrow(() -> new BlueException(INVALID_PARAM)));

    /**
     * login
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> login(ServerRequest serverRequest) {
        return loginHandler.apply(serverRequest);
    }

}
