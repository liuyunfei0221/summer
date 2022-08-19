package com.blue.shine.handler.manager;

import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.shine.model.ShineInsertParam;
import com.blue.shine.model.ShineUpdateParam;
import com.blue.shine.service.inter.ShineService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.base.AccessGetter.getAccessReact;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.common.base.PathVariableGetter.getLongVariableReact;
import static com.blue.basic.constant.common.PathVariable.ID;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.shine.constant.ShineTypeReference.PAGE_MODEL_FOR_SHINE_CONDITION_TYPE;
import static com.blue.shine.constant.ShineTypeReference.SCROLL_MODEL_WITH_SNAP_SHOT_FOR_SHINE_CONDITION_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;
import static reactor.core.publisher.Mono.error;

/**
 * shine manager handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class ShineManagerHandler {

    private final ShineService shineService;

    public ShineManagerHandler(ShineService shineService) {
        this.shineService = shineService;
    }

    /**
     * create a new shine
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> insert(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(ShineInsertParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> shineService.insertShine(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(si ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(si, serverRequest), BlueResponse.class));
    }

    /**
     * update shine
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(ShineUpdateParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> shineService.updateShine(tuple2.getT1(), tuple2.getT2().getId()))
                .flatMap(si ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(si, serverRequest), BlueResponse.class));
    }

    /**
     * delete shine
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        return getLongVariableReact(serverRequest, ID.key)
                .flatMap(shineService::deleteShine)
                .flatMap(si ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(si, serverRequest), BlueResponse.class));
    }

    /**
     * select shine
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> scrollBaseOnSnapShot(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SCROLL_MODEL_WITH_SNAP_SHOT_FOR_SHINE_CONDITION_TYPE)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(shineService::selectShineInfoScrollMonoByScrollAndCursorBaseOnSnapShot)
                .flatMap(pmr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(pmr, serverRequest), BlueResponse.class));
    }

    /**
     * select shine
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> page(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PAGE_MODEL_FOR_SHINE_CONDITION_TYPE)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(shineService::selectShineInfoPageMonoByPageAndCondition)
                .flatMap(pmr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(pmr, serverRequest), BlueResponse.class));
    }

}
