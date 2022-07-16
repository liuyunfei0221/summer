package com.blue.auth.handler.manager;

import com.blue.auth.api.model.MemberRoleRelationInsertOrDeleteParam;
import com.blue.auth.api.model.MemberRoleRelationUpdateParam;
import com.blue.auth.api.model.RoleResRelationParam;
import com.blue.auth.service.inter.AuthControlService;
import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.basic.common.reactive.ReactiveCommonFunctions.success;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
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
                .flatMap(a ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(a), BlueResponse.class));
    }

    /**
     * insert member-role-relation
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> insertAuthorityByMember(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(MemberRoleRelationInsertOrDeleteParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> authControlService.insertAuthorityByMember(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(a ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(a), BlueResponse.class));
    }

    /**
     * update member-role-relation
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> updateAuthoritiesByMember(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(MemberRoleRelationUpdateParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> authControlService.updateAuthoritiesByMember(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(a ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(a), BlueResponse.class));
    }

    /**
     * delete member-role-relation
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> deleteAuthorityByMember(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(MemberRoleRelationInsertOrDeleteParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> authControlService.deleteAuthorityByMember(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(a ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(a), BlueResponse.class));
    }

}
