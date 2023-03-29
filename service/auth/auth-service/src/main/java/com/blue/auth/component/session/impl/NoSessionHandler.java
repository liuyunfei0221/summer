package com.blue.auth.component.session.impl;

import com.blue.auth.component.session.inter.SessionHandler;
import com.blue.auth.model.LoginParam;
import com.blue.basic.constant.auth.CredentialType;
import com.blue.basic.model.exps.BlueException;
import org.slf4j.Logger;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.constant.auth.CredentialType.NOT_LOGGED_IN;
import static com.blue.basic.constant.common.ResponseElement.FORBIDDEN;
import static org.slf4j.LoggerFactory.getLogger;
import static reactor.core.publisher.Mono.error;

/**
 * no session handler,for temp visitor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"DuplicatedCode", "JavaDoc", "unused"})
public class NoSessionHandler implements SessionHandler {

    private static final Logger LOGGER = getLogger(NoSessionHandler.class);

    /**
     * Whether to support visitors
     *
     * @param loginParam
     * @param serverRequest
     * @return
     */
    @Override
    public Mono<ServerResponse> login(LoginParam loginParam, ServerRequest serverRequest) {
        LOGGER.info("loginParam = {}", loginParam);

        return error(() -> new BlueException(FORBIDDEN));
    }

    @Override
    public CredentialType targetType() {
        return NOT_LOGGED_IN;
    }

}
