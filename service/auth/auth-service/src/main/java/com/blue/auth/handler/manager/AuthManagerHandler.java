package com.blue.auth.handler.manager;

import com.blue.auth.service.inter.ControlService;
import com.blue.base.model.base.BlueResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.base.common.reactive.PathVariableGetter.getLongVariableReact;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.PathVariable.MID;
import static com.blue.base.constant.base.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
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
        return zip(getLongVariableReact(serverRequest, MID.key),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> controlService.invalidateAuthByMember(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(ri ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, ri, serverRequest), BlueResponse.class));
    }

    /**
     * select security infos
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> selectSecurityInfos(ServerRequest serverRequest) {
        return zip(getLongVariableReact(serverRequest, MID.key),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> controlService.selectSecurityInfoMonoByMemberId(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(res ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, res, serverRequest), BlueResponse.class));
    }

}
