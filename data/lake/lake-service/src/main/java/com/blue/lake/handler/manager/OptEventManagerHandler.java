package com.blue.lake.handler.manager;

import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.lake.model.OptEventCondition;
import com.blue.lake.service.inter.OptEventService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.lake.constant.LakeTypeReference.SCROLL_MODEL_FOR_OPT_EVENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.defer;
import static reactor.core.publisher.Mono.error;

/**
 * opt event manager handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc"})
@Component
public class OptEventManagerHandler {

    private final OptEventService optEventService;

    public OptEventManagerHandler(OptEventService optEventService) {
        this.optEventService = optEventService;
    }

    /**
     * select events
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> scroll(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SCROLL_MODEL_FOR_OPT_EVENT_TYPE)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(optEventService::selectOptEventScrollByConditionAndCursor)
                .flatMap(smr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(smr, serverRequest), BlueResponse.class));
    }

    /**
     * count events
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> count(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(OptEventCondition.class)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(optEventService::countOptEventByCondition)
                .flatMap(ccr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ccr, serverRequest), BlueResponse.class));
    }

}