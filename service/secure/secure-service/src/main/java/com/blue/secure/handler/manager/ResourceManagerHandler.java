package com.blue.secure.handler.manager;

import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.base.IdentityParam;
import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.exps.BlueException;
import com.blue.secure.model.ResourceInsertParam;
import com.blue.secure.model.ResourceUpdateParam;
import com.blue.secure.service.inter.ControlService;
import com.blue.secure.service.inter.ResourceService;
import com.blue.secure.service.inter.RoleResRelationService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.base.common.reactive.PathVariableGetter.getLongVariableReact;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.PathVariable.ID;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.OK;
import static com.blue.base.constant.base.ResponseMessage.EMPTY_PARAM;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.zip;

/**
 * role manager handler
 *
 * @author liuyunfei
 * @date 2021/8/31
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
@Component
public final class ResourceManagerHandler {

    private final ControlService controlService;

    private final ResourceService resourceService;

    private final RoleResRelationService roleResRelationService;

    public ResourceManagerHandler(ControlService controlService, ResourceService resourceService, RoleResRelationService roleResRelationService) {
        this.controlService = controlService;
        this.resourceService = resourceService;
        this.roleResRelationService = roleResRelationService;
    }

    /**
     * create a new resource
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> insert(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(ResourceInsertParam.class)
                        .switchIfEmpty(error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PARAM.message))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> controlService.insertResource(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(ri ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, ri, OK.message), BlueResponse.class));
    }

    /**
     * update resource
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(ResourceUpdateParam.class)
                        .switchIfEmpty(error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PARAM.message))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> controlService.updateResource(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(ri ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, ri, OK.message), BlueResponse.class));
    }

    /**
     * delete resource
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        return zip(getLongVariableReact(serverRequest, ID.key), getAccessReact(serverRequest))
                .flatMap(tuple2 -> controlService.deleteResource(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(ri ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, ri, OK.message), BlueResponse.class));
    }

    /**
     * select resources
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> select(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PageModelRequest.class)
                .switchIfEmpty(error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PARAM.message)))
                .flatMap(resourceService::selectResourceInfoPageMonoByPageAndCondition)
                .flatMap(vo ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, vo, OK.message), BlueResponse.class));
    }

    /**
     * get authority base on resource by res id
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> selectAuthority(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(IdentityParam.class)
                .switchIfEmpty(error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PARAM.message)))
                .flatMap(wrapper ->
                        roleResRelationService.selectAuthorityMonoByResId(wrapper.getId()))
                .flatMap(auth ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, auth, OK.message), BlueResponse.class));

    }

}