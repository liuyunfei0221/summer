package com.blue.secure.handler.manager;

import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.base.IdentityWrapper;
import com.blue.base.model.base.PageModelRequest;
import com.blue.secure.service.inter.ResourceService;
import com.blue.secure.service.inter.RoleResRelationService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.CommonException.EMPTY_PARAM_EXP;
import static com.blue.base.constant.base.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;

/**
 * @author liuyunfei
 * @date 2021/8/31
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
@Component
public final class ResourceManagerHandler {

    private final ResourceService resourceService;

    private final RoleResRelationService roleResRelationService;

    public ResourceManagerHandler(ResourceService resourceService, RoleResRelationService roleResRelationService) {
        this.resourceService = resourceService;
        this.roleResRelationService = roleResRelationService;
    }

    /**
     * select resources
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> select(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PageModelRequest.class)
                .switchIfEmpty(error(EMPTY_PARAM_EXP.exp))
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
        return serverRequest.bodyToMono(IdentityWrapper.class)
                .switchIfEmpty(error(EMPTY_PARAM_EXP.exp))
                .flatMap(wrapper ->
                        roleResRelationService.selectAuthorityMonoByResId(wrapper.getId()))
                .flatMap(auth ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, auth, OK.message), BlueResponse.class));

    }

}