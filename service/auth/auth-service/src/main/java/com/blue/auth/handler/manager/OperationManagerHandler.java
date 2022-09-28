package com.blue.auth.handler.manager;

import com.blue.auth.model.AuthenticationParam;
import com.blue.auth.model.EncryptedDataParam;
import com.blue.auth.service.inter.AuthService;
import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.defer;
import static reactor.core.publisher.Mono.error;

/**
 * operation manager handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public class OperationManagerHandler {

    private final AuthService authService;

    public OperationManagerHandler(AuthService authService) {
        this.authService = authService;
    }

    /**
     * jwt -> payload
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> payload(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(AuthenticationParam.class)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(ap -> authService.parsePayload(ap.getAuthentication()))
                .flatMap(p ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(p, serverRequest), BlueResponse.class));
    }

    /**
     * jwt -> access
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> access(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(AuthenticationParam.class)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(ap -> authService.parseAccess(ap.getAuthentication()))
                .flatMap(ac ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ac, serverRequest), BlueResponse.class));
    }

    /**
     * jwt -> session
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> session(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(AuthenticationParam.class)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(ap -> authService.parseSession(ap.getAuthentication()))
                .flatMap(sn ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(sn, serverRequest), BlueResponse.class));
    }

    /**
     * encrypted -> data
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> encrypted(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(EncryptedDataParam.class)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(authService::parseEncrypted)
                .flatMap(d ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(d, serverRequest), BlueResponse.class));
    }

}
