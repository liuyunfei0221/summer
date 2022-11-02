package com.blue.base.handler.api;

import com.blue.base.service.inter.StateService;
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
 * state api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public class StateApiHandler {

    private final StateService stateService;

    public StateApiHandler(StateService stateService) {
        this.stateService = stateService;
    }

    /**
     * select states by country
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> selectByCountryId(ServerRequest serverRequest) {
        return getLongVariableReact(serverRequest, PID.key)
                .flatMap(stateService::selectStateInfoByCountryId)
                .flatMap(l ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(l, serverRequest), BlueResponse.class));
    }

}
