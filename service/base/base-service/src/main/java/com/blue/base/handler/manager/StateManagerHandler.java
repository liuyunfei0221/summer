package com.blue.base.handler.manager;

import com.blue.base.model.StateInsertParam;
import com.blue.base.model.StateUpdateParam;
import com.blue.base.service.inter.RegionControlService;
import com.blue.base.service.inter.StateService;
import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.constant.BaseTypeReference.PAGE_MODEL_FOR_STATE_CONDITION_TYPE;
import static com.blue.basic.common.base.PathVariableGetter.getLongVariableReact;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.constant.common.PathVariable.ID;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.defer;
import static reactor.core.publisher.Mono.error;

/**
 * state manager handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavadocDeclaration")
@Component
public class StateManagerHandler {

    private final StateService stateService;

    private final RegionControlService regionControlService;

    public StateManagerHandler(StateService stateService, RegionControlService regionControlService) {
        this.stateService = stateService;
        this.regionControlService = regionControlService;
    }

    /**
     * create a new state
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> insert(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(StateInsertParam.class)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(regionControlService::insertState)
                .flatMap(si ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(si, serverRequest), BlueResponse.class));
    }

    /**
     * update state
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(StateUpdateParam.class)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(regionControlService::updateState)
                .flatMap(si ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(si, serverRequest), BlueResponse.class));
    }

    /**
     * delete state
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        return getLongVariableReact(serverRequest, ID.key)
                .flatMap(regionControlService::deleteState)
                .flatMap(si ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(si, serverRequest), BlueResponse.class));
    }

    /**
     * select states
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> select(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PAGE_MODEL_FOR_STATE_CONDITION_TYPE)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(stateService::selectStatePageMonoByPageAndCondition)
                .flatMap(pms ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(pms, serverRequest), BlueResponse.class));
    }

}
