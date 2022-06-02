package com.blue.auth.handler.manager;

import com.blue.auth.api.model.MemberRoleRelationParam;
import com.blue.auth.api.model.RoleResRelationParam;
import com.blue.auth.service.inter.AuthControlService;
import com.blue.base.model.common.BlueResponse;
import com.blue.base.model.exps.BlueException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.AccessGetterForReactive.*;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.base.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * relation manager handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public class RelationManagerHandler {

    private final AuthControlService authControlService;

    public RelationManagerHandler(AuthControlService authControlService) {
        this.authControlService = authControlService;
    }

    /**
     * update role-res-relations
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> updateAuthorityByRole(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(RoleResRelationParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> authControlService.updateAuthorityByRole(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(ri ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, ri, serverRequest), BlueResponse.class));
    }

    /**
     * update member-role-relation
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> updateAuthorityByMember(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(MemberRoleRelationParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> authControlService.updateAuthorityByMember(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(ri ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, ri, serverRequest), BlueResponse.class));
    }

}
