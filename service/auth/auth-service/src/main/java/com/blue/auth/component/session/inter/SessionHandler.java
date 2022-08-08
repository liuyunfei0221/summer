package com.blue.auth.component.session.inter;

import com.blue.basic.constant.auth.CredentialType;
import com.blue.auth.model.LoginParam;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * session handler interface
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface SessionHandler {

    /**
     * session
     *
     * @param loginParam
     * @param serverRequest
     * @return
     */
    Mono<ServerResponse> login(LoginParam loginParam, ServerRequest serverRequest);

    /**
     * target credential type to process
     *
     * @return
     */
    CredentialType targetType();

}
