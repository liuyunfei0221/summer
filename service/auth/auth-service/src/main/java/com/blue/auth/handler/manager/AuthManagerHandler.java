package com.blue.auth.handler.manager;

import com.blue.auth.service.inter.AuthControlService;
import com.blue.basic.model.common.BlueResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.access.AccessGetter.getAccessReact;
import static com.blue.basic.common.base.PathVariableGetter.getLongVariableReact;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.constant.common.PathVariable.MID;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.zip;

/**
 * auth manager handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class AuthManagerHandler {

    private final AuthControlService authControlService;

    public AuthManagerHandler(AuthControlService authControlService) {
        this.authControlService = authControlService;
    }

    /**
     * invalid member auth by member id
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> invalidateAuthByMember(ServerRequest serverRequest) {
        return zip(getLongVariableReact(serverRequest, MID.key),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> authControlService.invalidateAuthByMemberId(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(ri ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ri, serverRequest), BlueResponse.class));
    }

    /**
     * select security info
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> selectSecurityInfos(ServerRequest serverRequest) {
        return zip(getLongVariableReact(serverRequest, MID.key),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> authControlService.selectSecurityInfoByMemberId(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(res ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(res, serverRequest), BlueResponse.class));
    }

}
