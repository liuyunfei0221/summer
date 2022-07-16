package com.blue.base.handler.api;

import com.blue.base.service.inter.CityService;
import com.blue.basic.model.common.BlueResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.reactive.PathVariableGetter.getLongVariableReact;
import static com.blue.basic.common.reactive.ReactiveCommonFunctions.success;
import static com.blue.basic.constant.common.PathVariable.PID;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * city api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public class CityApiHandler {

    private final CityService cityService;

    public CityApiHandler(CityService cityService) {
        this.cityService = cityService;
    }

    /**
     * select cities by state
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> selectByStateId(ServerRequest serverRequest) {
        return getLongVariableReact(serverRequest, PID.key)
                .flatMap(cityService::selectCityInfoMonoByStateId)
                .flatMap(l ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(l), BlueResponse.class));
    }

}
