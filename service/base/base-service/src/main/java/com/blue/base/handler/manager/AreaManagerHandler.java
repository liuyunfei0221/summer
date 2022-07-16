package com.blue.base.handler.manager;

import com.blue.base.model.AreaInsertParam;
import com.blue.base.model.AreaUpdateParam;
import com.blue.base.service.inter.AreaService;
import com.blue.base.service.inter.RegionControlService;
import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.constant.BaseTypeReference.PAGE_MODEL_FOR_AREA_CONDITION_TYPE;
import static com.blue.basic.common.reactive.PathVariableGetter.getLongVariableReact;
import static com.blue.basic.common.reactive.ReactiveCommonFunctions.success;
import static com.blue.basic.constant.common.PathVariable.ID;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
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

    private final RegionControlService regionControlService;

    public AreaManagerHandler(AreaService areaService, RegionControlService regionControlService) {
        this.areaService = areaService;
        this.regionControlService = regionControlService;
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
                .flatMap(regionControlService::insertArea)
                .flatMap(ai ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ai), BlueResponse.class));
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
                .flatMap(regionControlService::updateArea)
                .flatMap(ai ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ai), BlueResponse.class));
    }

    /**
     * delete area
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        return getLongVariableReact(serverRequest, ID.key)
                .flatMap(regionControlService::deleteArea)
                .flatMap(ai ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ai), BlueResponse.class));
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
                                .body(success(pma), BlueResponse.class));
    }

}
