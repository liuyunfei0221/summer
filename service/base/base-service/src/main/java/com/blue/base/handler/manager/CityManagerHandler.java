package com.blue.base.handler.manager;

import com.blue.base.model.CityInsertParam;
import com.blue.base.model.CityUpdateParam;
import com.blue.base.service.inter.CityService;
import com.blue.base.service.inter.RegionControlService;
import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.constant.BaseTypeReference.PAGE_MODEL_FOR_CITY_CONDITION_TYPE;
import static com.blue.basic.common.base.PathVariableGetter.getLongVariableReact;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.constant.common.PathVariable.ID;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.defer;
import static reactor.core.publisher.Mono.error;

/**
 * city manager handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavadocDeclaration")
@Component
public class CityManagerHandler {

    private final CityService cityService;

    private final RegionControlService regionControlService;

    public CityManagerHandler(CityService cityService, RegionControlService regionControlService) {
        this.cityService = cityService;
        this.regionControlService = regionControlService;
    }

    /**
     * create a new city
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> insert(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CityInsertParam.class)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(regionControlService::insertCity)
                .flatMap(ci ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ci, serverRequest), BlueResponse.class));
    }

    /**
     * update city
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CityUpdateParam.class)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(regionControlService::updateCity)
                .flatMap(ci ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ci, serverRequest), BlueResponse.class));
    }

    /**
     * delete city
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        return getLongVariableReact(serverRequest, ID.key)
                .flatMap(regionControlService::deleteCity)
                .flatMap(ci ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ci, serverRequest), BlueResponse.class));
    }

    /**
     * select cities
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> page(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PAGE_MODEL_FOR_CITY_CONDITION_TYPE)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(cityService::selectCityPageMonoByPageAndCondition)
                .flatMap(pmr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(pmr, serverRequest), BlueResponse.class));
    }

}
