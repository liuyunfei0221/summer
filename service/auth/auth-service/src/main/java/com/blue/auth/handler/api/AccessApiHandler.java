package com.blue.auth.handler.api;

import com.blue.auth.model.AccessResetParam;
import com.blue.auth.model.AccessUpdateParam;
import com.blue.auth.service.inter.AuthControlService;
import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.access.AccessGetter.getAccessReact;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * access api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class AccessApiHandler {

    private final AuthControlService authControlService;

    public AccessApiHandler(AuthControlService authControlService) {
        this.authControlService = authControlService;
    }

    /**
     * update member's access/password
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> updateAccess(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(AccessUpdateParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 ->
                        authControlService.updateAccessByAccess(tuple2.getT1(), tuple2.getT2()))
                .flatMap(ig ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(serverRequest), BlueResponse.class));
    }

    /**
     * reset member's access/password
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> resetAccess(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(AccessResetParam.class)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(authControlService::resetAccessByAccess)
                .flatMap(ig ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(serverRequest), BlueResponse.class));
    }

}