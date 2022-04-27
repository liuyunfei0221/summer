package com.blue.auth.handler.manager;

import com.blue.auth.model.RoleInsertParam;
import com.blue.auth.model.RoleUpdateParam;
import com.blue.auth.service.inter.ControlService;
import com.blue.auth.service.inter.RoleResRelationService;
import com.blue.auth.service.inter.RoleService;
import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.base.IdentityParam;
import com.blue.base.model.exps.BlueException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.auth.constant.AuthTypeReference.PAGE_MODEL_FOR_ROLE_CONDITION_TYPE;
import static com.blue.base.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.base.common.reactive.PathVariableGetter.getLongVariableReact;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.PathVariable.ID;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.base.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.zip;

/**
 * role manager handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class RoleManagerHandler {

    private final ControlService controlService;

    private final RoleService roleService;

    private final RoleResRelationService roleResRelationService;

    public RoleManagerHandler(ControlService controlService, RoleService roleService, RoleResRelationService roleResRelationService) {
        this.controlService = controlService;
        this.roleService = roleService;
        this.roleResRelationService = roleResRelationService;
    }

    /**
     * create a new role
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> insert(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(RoleInsertParam.class)
                        .switchIfEmpty(error(() -> new BlueException(EMPTY_PARAM))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> controlService.insertRole(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(ri ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, ri, serverRequest), BlueResponse.class));
    }

    /**
     * update role
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(RoleUpdateParam.class)
                        .switchIfEmpty(error(() -> new BlueException(EMPTY_PARAM))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> controlService.updateRole(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(ri ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, ri, serverRequest), BlueResponse.class));
    }

    /**
     * delete role
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        return zip(getLongVariableReact(serverRequest, ID.key), getAccessReact(serverRequest))
                .flatMap(tuple2 -> controlService.deleteRole(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(ri ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, ri, serverRequest), BlueResponse.class));
    }

    /**
     * update default role
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> updateDefault(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(IdentityParam.class)
                        .switchIfEmpty(error(() -> new BlueException(EMPTY_PARAM)))
                , getAccessReact(serverRequest))
                .flatMap(tuple2 -> controlService.updateDefaultRole(tuple2.getT1().getId(), tuple2.getT2().getId()))
                .flatMap(ri ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, ri, serverRequest), BlueResponse.class));
    }

    /**
     * select roles
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> select(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PAGE_MODEL_FOR_ROLE_CONDITION_TYPE)
                .switchIfEmpty(error(() -> new BlueException(EMPTY_PARAM)))
                .flatMap(roleService::selectRoleInfoPageMonoByPageAndCondition)
                .flatMap(pmr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, pmr, serverRequest), BlueResponse.class));
    }

    /**
     * get authority base on role by role id
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> selectAuthority(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(IdentityParam.class)
                .switchIfEmpty(error(() -> new BlueException(EMPTY_PARAM)))
                .flatMap(ip ->
                        controlService.selectAuthorityMonoByRoleId(ip.getId()))
                .flatMap(auth ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, auth, serverRequest), BlueResponse.class));
    }

}
