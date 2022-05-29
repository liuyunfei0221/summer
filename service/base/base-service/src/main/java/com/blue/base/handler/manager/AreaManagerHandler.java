package com.blue.base.handler.manager;

import com.blue.base.model.AreaInsertParam;
import com.blue.base.model.AreaUpdateParam;
import com.blue.base.model.common.BlueResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.base.service.inter.AreaService;
import com.blue.base.service.inter.ControlService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.PathVariableGetter.getLongVariableReact;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.BaseTypeReference.PAGE_MODEL_FOR_AREA_CONDITION_TYPE;
import static com.blue.base.constant.base.PathVariable.ID;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.base.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.defer;
import static reactor.core.publisher.Mono.error;

/**
 * area manager handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavadocDeclaration")
@Component
public class AreaManagerHandler {

    private final AreaService areaService;

    private final ControlService controlService;

    public AreaManagerHandler(AreaService areaService, ControlService controlService) {
        this.areaService = areaService;
        this.controlService = controlService;
    }

    /**
     * create a new area
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> insert(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(AreaInsertParam.class)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(controlService::insertArea)
                .flatMap(ai ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, ai, serverRequest), BlueResponse.class));
    }

    /**
     * update area
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(AreaUpdateParam.class)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(controlService::updateArea)
                .flatMap(ai ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, ai, serverRequest), BlueResponse.class));
    }

    /**
     * delete area
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        return getLongVariableReact(serverRequest, ID.key)
                .flatMap(controlService::deleteArea)
                .flatMap(ai ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, ai, serverRequest), BlueResponse.class));
    }

    /**
     * select areas
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> select(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PAGE_MODEL_FOR_AREA_CONDITION_TYPE)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(areaService::selectAreaPageMonoByPageAndCondition)
                .flatMap(pma ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, pma, serverRequest), BlueResponse.class));
    }

}
