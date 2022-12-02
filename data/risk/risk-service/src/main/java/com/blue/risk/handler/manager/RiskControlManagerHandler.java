package com.blue.risk.handler.manager;

import com.blue.basic.model.common.*;
import com.blue.basic.model.exps.BlueException;
import com.blue.risk.model.IllegalMarkParam;
import com.blue.risk.service.inter.RiskControlService;
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
 * risk control manager handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class RiskControlManagerHandler {

    private final RiskControlService riskControlService;

    public RiskControlManagerHandler(RiskControlService riskControlService) {
        this.riskControlService = riskControlService;
    }

    /**
     * illegal mark
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> illegalMark(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(IllegalMarkParam.class)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(riskControlService::illegalMarkByParam)
                .flatMap(success ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(success, serverRequest), BlueResponse.class));
    }

    /**
     * invalid auth by member id
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> invalidateAuth(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(IdentityParam.class)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(identityParam -> riskControlService.invalidateAuth(identityParam.getId()))
                .flatMap(success ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(success, serverRequest), BlueResponse.class));
    }

    /**
     * invalid auth by member ids
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> invalidateAuthBatch(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(IdentitiesParam.class)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(identityParam -> riskControlService.invalidateAuthBatch(identityParam.getIds()))
                .flatMap(success ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(success, serverRequest), BlueResponse.class));
    }

    /**
     * update member status
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> updateMemberBasicStatus(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(IdentityAndStatusParam.class)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(identityAndStatusParam ->
                        riskControlService.updateMemberBasicStatus(identityAndStatusParam.getId(), identityAndStatusParam.getStatus()))
                .flatMap(mbi ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(mbi, serverRequest), BlueResponse.class));
    }

    /**
     * update member status batch
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> updateMemberBasicStatusBatch(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(IdentitiesAndStatusParam.class)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(identitiesAndStatusParam ->
                        riskControlService.updateMemberBasicStatusBatch(identitiesAndStatusParam.getIds(), identitiesAndStatusParam.getStatus()))
                .flatMap(mbi ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(mbi, serverRequest), BlueResponse.class));
    }

}
