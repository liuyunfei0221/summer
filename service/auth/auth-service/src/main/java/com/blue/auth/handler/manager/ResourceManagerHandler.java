package com.blue.auth.handler.manager;

import com.blue.auth.model.ResourceInsertParam;
import com.blue.auth.model.ResourceUpdateParam;
import com.blue.auth.service.inter.AuthControlService;
import com.blue.auth.service.inter.ResourceService;
import com.blue.auth.service.inter.RoleResRelationService;
import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.common.IdentityParam;
import com.blue.basic.model.exps.BlueException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.auth.constant.AuthTypeReference.PAGE_MODEL_FOR_RESOURCE_CONDITION_TYPE;
import static com.blue.basic.common.base.AccessGetter.getAccessReact;
import static com.blue.basic.common.base.PathVariableGetter.getLongVariableReact;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.constant.common.PathVariable.ID;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * role manager handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class ResourceManagerHandler {

    private final AuthControlService authControlService;

    private final ResourceService resourceService;

    private final RoleResRelationService roleResRelationService;

    public ResourceManagerHandler(AuthControlService authControlService, ResourceService resourceService, RoleResRelationService roleResRelationService) {
        this.authControlService = authControlService;
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
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> authControlService.insertResource(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(ri ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ri, serverRequest), BlueResponse.class));
    }

    /**
     * update resource
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(ResourceUpdateParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> authControlService.updateResource(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(ri ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ri, serverRequest), BlueResponse.class));
    }

    /**
     * delete resource
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        return zip(getLongVariableReact(serverRequest, ID.key), getAccessReact(serverRequest))
                .flatMap(tuple2 -> authControlService.deleteResource(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(ri ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ri, serverRequest), BlueResponse.class));
    }

    /**
     * select resources
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> page(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PAGE_MODEL_FOR_RESOURCE_CONDITION_TYPE)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(resourceService::selectResourceManagerInfoPageByPageAndCondition)
                .flatMap(pmr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(pmr, serverRequest), BlueResponse.class));
    }

    /**
     * get authority base on resource by res id
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> selectAuthority(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(IdentityParam.class)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(ip ->
                        roleResRelationService.selectAuthorityByResId(ip.getId()))
                .flatMap(auth ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(auth, serverRequest), BlueResponse.class));

    }

}