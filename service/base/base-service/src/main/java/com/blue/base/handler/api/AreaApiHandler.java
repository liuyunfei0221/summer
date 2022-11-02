package com.blue.base.handler.api;

import com.blue.base.service.inter.AreaService;
import com.blue.basic.model.common.BlueResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.base.PathVariableGetter.getLongVariableReact;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.constant.common.PathVariable.PID;
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
                .flatMap(areaService::selectAreaInfoByCityId)
                .flatMap(l ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(l, serverRequest), BlueResponse.class));
    }

}
