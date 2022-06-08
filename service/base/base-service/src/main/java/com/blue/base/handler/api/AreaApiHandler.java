package com.blue.base.handler.api;

import com.blue.base.model.common.BlueResponse;
import com.blue.base.service.inter.AreaService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.PathVariableGetter.getLongVariableReact;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.common.PathVariable.PID;
import static com.blue.base.constant.common.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

/**
 * area api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public class AreaApiHandler {

    private final AreaService areaService;

    public AreaApiHandler(AreaService areaService) {
        this.areaService = areaService;
    }

    /**
     * select areas by city
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> selectByCityId(ServerRequest serverRequest) {
        return getLongVariableReact(serverRequest, PID.key)
                .flatMap(areaService::selectAreaInfoMonoByCityId)
                .flatMap(l ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, l, serverRequest), BlueResponse.class));
    }

}
