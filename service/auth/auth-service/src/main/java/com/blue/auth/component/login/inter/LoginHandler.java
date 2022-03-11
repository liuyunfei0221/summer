package com.blue.auth.component.login.inter;

import com.blue.base.constant.auth.LoginType;
import com.blue.auth.model.LoginParam;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * login handler interface
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface LoginHandler {

    /**
     * login
     *
     * @param loginParam
     * @param serverRequest
     * @return
     */
    Mono<ServerResponse> login(LoginParam loginParam, ServerRequest serverRequest);

    /**
     * target login type to process
     *
     * @return
     */
    LoginType targetType();

}
