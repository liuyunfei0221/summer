package com.blue.auth.handler.manager;

import com.blue.auth.service.inter.ControlService;
import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.base.IdentityParam;
import com.blue.base.model.exps.BlueException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.base.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.zip;

/**
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class AuthManagerHandler {

    private final ControlService controlService;

    public AuthManagerHandler(ControlService controlService) {
        this.controlService = controlService;
    }

    /**
     * invalid member auth by member id
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> invalidateAuthByMember(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(IdentityParam.class)
                        .switchIfEmpty(error(() -> new BlueException(EMPTY_PARAM))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> controlService.invalidateAuthByMember(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(ri ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, ri, serverRequest), BlueResponse.class));
    }

}
