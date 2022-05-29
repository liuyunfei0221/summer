package com.blue.base.handler.manager;

import com.blue.base.model.CountryInsertParam;
import com.blue.base.model.CountryUpdateParam;
import com.blue.base.model.common.BlueResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.base.service.inter.ControlService;
import com.blue.base.service.inter.CountryService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.PathVariableGetter.getLongVariableReact;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.BaseTypeReference.PAGE_MODEL_FOR_COUNTRY_CONDITION_TYPE;
import static com.blue.base.constant.base.PathVariable.ID;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.base.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.defer;
import static reactor.core.publisher.Mono.error;

/**
 * country manager handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavadocDeclaration")
@Component
public class CountryManagerHandler {

    private final CountryService countryService;

    private final ControlService controlService;

    public CountryManagerHandler(CountryService countryService, ControlService controlService) {
        this.countryService = countryService;
        this.controlService = controlService;
    }

    /**
     * create a new country
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> insert(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CountryInsertParam.class)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(controlService::insertCountry)
                .flatMap(ci ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, ci, serverRequest), BlueResponse.class));
    }

    /**
     * update country
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(CountryUpdateParam.class)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(controlService::updateCountry)
                .flatMap(ci ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, ci, serverRequest), BlueResponse.class));
    }

    /**
     * delete country
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        return getLongVariableReact(serverRequest, ID.key)
                .flatMap(controlService::deleteCountry)
                .flatMap(ci ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, ci, serverRequest), BlueResponse.class));
    }

    /**
     * select countries
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> select(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PAGE_MODEL_FOR_COUNTRY_CONDITION_TYPE)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(countryService::selectCountryPageMonoByPageAndCondition)
                .flatMap(pmc ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, pmc, serverRequest), BlueResponse.class));
    }

}
