package com.blue.auth.handler.api;

import com.blue.auth.model.CredentialModifyParam;
import com.blue.auth.model.CredentialSettingUpParam;
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
 * credential api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class CredentialApiHandler {

    private final AuthControlService authControlService;

    public CredentialApiHandler(AuthControlService authControlService) {
        this.authControlService = authControlService;
    }

    /**
     * add new credential base on verify type for a member
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> insertCredential(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(CredentialSettingUpParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 ->
                        just(authControlService.insertCredential(tuple2.getT1(), tuple2.getT2())))
                .flatMap(mbi ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(mbi, serverRequest), BlueResponse.class));
    }

    /**
     * update credential
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> updateCredential(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(CredentialModifyParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 ->
                        just(authControlService.updateCredential(tuple2.getT1(), tuple2.getT2())))
                .flatMap(mbi ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(mbi, serverRequest), BlueResponse.class));
    }

}