package com.blue.secure.handler.manager;

import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.base.IdentityWrapper;
import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.exps.BlueException;
import com.blue.secure.model.RoleInsertParam;
import com.blue.secure.service.inter.ControlService;
import com.blue.secure.service.inter.RoleResRelationService;
import com.blue.secure.service.inter.RoleService;
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
import static reactor.core.publisher.Mono.*;

/**
 * @author liuyunfei
 * @date 2021/8/31
 * @apiNote
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
                        .switchIfEmpty(error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PARAM.message))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> just(controlService.insertRole(tuple2.getT1(), tuple2.getT2().getId())))
                .flatMap(mi ->
                        ok()
                                .contentType(APPLICATION_JSON)
                                .body(generate(OK.code, mi, OK.message), BlueResponse.class));
    }

    /**
     * select roles
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> select(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PageModelRequest.class)
                .switchIfEmpty(error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PARAM.message)))
                .flatMap(roleService::selectRoleInfoPageMonoByPageAndCondition)
                .flatMap(vo ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, vo, OK.message), BlueResponse.class));
    }

    /**
     * get authority base on role by role id
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> selectAuthority(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(IdentityWrapper.class)
                .switchIfEmpty(error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PARAM.message)))
                .flatMap(wrapper ->
                        roleResRelationService.selectAuthorityMonoByRoleId(wrapper.getId()))
                .flatMap(auth ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, auth, OK.message), BlueResponse.class));
    }

}
