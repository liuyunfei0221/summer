package com.blue.secure.handler.manager;

import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.secure.model.MemberRoleRelationParam;
import com.blue.secure.model.RoleResRelationParam;
import com.blue.secure.service.inter.ControlService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.OK;
import static com.blue.base.constant.base.ResponseMessage.EMPTY_PARAM;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.zip;

/**
 * relation manager handler
 *
 * @author liuyunfei
 * @date 2021/11/9
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
@Component
public class RelationManagerHandler {

    private final ControlService controlService;

    public RelationManagerHandler(ControlService controlService) {
        this.controlService = controlService;
    }

    /**
     * update role-res-relations
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> updateAuthorityByRole(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(RoleResRelationParam.class)
                        .switchIfEmpty(error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PARAM.message, null))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> controlService.updateAuthorityByRole(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(ri ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, ri, OK.message), BlueResponse.class));
    }

    /**
     * update member-role-relation
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> updateAuthorityByMember(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(MemberRoleRelationParam.class)
                        .switchIfEmpty(error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PARAM.message, null))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> controlService.updateAuthorityByMember(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(ri ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, ri, OK.message), BlueResponse.class));
    }

}
