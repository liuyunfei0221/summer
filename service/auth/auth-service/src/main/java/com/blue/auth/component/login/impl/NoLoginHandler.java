package com.blue.auth.component.login.impl;

import com.blue.auth.component.login.inter.LoginHandler;
import com.blue.auth.model.LoginParam;
import com.blue.base.constant.auth.CredentialType;
import com.blue.base.model.exps.BlueException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import static com.blue.base.constant.auth.CredentialType.NOT_LOGGED_IN;
import static com.blue.base.constant.base.ResponseElement.FORBIDDEN;
import static reactor.core.publisher.Mono.error;
import static reactor.util.Loggers.getLogger;

/**
 * no login handler,for temp visitor
 *
 * @author DarkBlue
 */
@SuppressWarnings({"DuplicatedCode", "JavaDoc", "unused"})
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
    public CredentialType targetType() {
        return NOT_LOGGED_IN;
    }

}
