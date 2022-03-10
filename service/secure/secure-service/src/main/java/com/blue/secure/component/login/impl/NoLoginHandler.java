package com.blue.secure.component.login.impl;

import com.blue.base.constant.secure.LoginType;
import com.blue.base.model.exps.BlueException;
import com.blue.secure.component.login.inter.LoginHandler;
import com.blue.secure.model.LoginParam;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import static com.blue.base.constant.base.ResponseElement.FORBIDDEN;
import static com.blue.base.constant.secure.LoginType.NOT_LOGGED_IN;
import static org.springframework.core.Ordered.LOWEST_PRECEDENCE;
import static reactor.core.publisher.Mono.error;
import static reactor.util.Loggers.getLogger;

/**
 * no login handler,for temp visitor
 *
 * @author DarkBlue
 */
@SuppressWarnings({"DuplicatedCode", "JavaDoc"})
@Component
@Order(LOWEST_PRECEDENCE - 1)
public class NoLoginHandler implements LoginHandler {

    private static final Logger LOGGER = getLogger(NoLoginHandler.class);

    /**
     * Whether to support visitors
     *
     * @param loginParam
     * @param serverRequest
     * @return
     */
    @Override
    public Mono<ServerResponse> login(LoginParam loginParam, ServerRequest serverRequest) {
        LOGGER.info("NoLoginHandler -> Mono<ServerResponse> login(LoginParam loginParam, ServerRequest serverRequest), loginParam = {}", loginParam);

        return error(() -> new BlueException(FORBIDDEN));
    }

    @Override
    public LoginType targetType() {
        return NOT_LOGGED_IN;
    }

}
