package com.blue.lake.handler.manager;

import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.base.LimitModelRequest;
import com.blue.base.model.exps.BlueException;
import com.blue.lake.service.inter.LakeService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.base.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;
import static reactor.util.Loggers.getLogger;

/**
 * @author liuyunfei
 * @date 2022/1/5
 * @apiNote
 */
@SuppressWarnings({"JavaDoc"})
@Component
public class EventManagerHandler {

    private static final Logger LOGGER = getLogger(EventManagerHandler.class);

    private final LakeService lakeService;

    public EventManagerHandler(LakeService lakeService) {
        this.lakeService = lakeService;
    }

    /**
     * test
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> listEvent(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(LimitModelRequest.class)
                .switchIfEmpty(error(() -> new BlueException(EMPTY_PARAM)))
                .flatMap(lakeService::selectByLimitAndRows)
                .flatMap(l ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, l, serverRequest), BlueResponse.class));
    }

}
