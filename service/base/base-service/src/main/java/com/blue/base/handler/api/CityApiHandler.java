package com.blue.base.handler.api;

import com.blue.base.model.base.BlueResponse;
import com.blue.base.service.inter.CityService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.PathVariableGetter.getLongVariableReact;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.PathVariable.PID;
import static com.blue.base.constant.base.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * city api handler
 *
 * @author liuyunfei
 * @date 2021/8/31
 * @apiNote
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
                                .body(generate(OK.code, l, serverRequest), BlueResponse.class));
    }

}
