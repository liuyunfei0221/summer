package com.blue.base.handler.manager;

import com.blue.base.model.StyleInsertParam;
import com.blue.base.model.StyleUpdateParam;
import com.blue.base.service.inter.StyleService;
import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.common.IdentityParam;
import com.blue.basic.model.exps.BlueException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.constant.BaseTypeReference.PAGE_MODEL_FOR_STYLE_CONDITION_TYPE;
import static com.blue.basic.common.base.AccessGetter.getAccessReact;
import static com.blue.basic.common.base.PathVariableGetter.getLongVariableReact;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.constant.common.PathVariable.ID;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * style manager handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class StyleManagerHandler {

    private final StyleService styleService;

    public StyleManagerHandler(StyleService styleService) {
        this.styleService = styleService;
    }

    /**
     * create a new style
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> insert(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(StyleInsertParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> just(styleService.insertStyle(tuple2.getT1(), tuple2.getT2().getId())))
                .flatMap(si ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(si, serverRequest), BlueResponse.class));
    }

    /**
     * update style
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(StyleUpdateParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> just(styleService.updateStyle(tuple2.getT1(), tuple2.getT2().getId())))
                .flatMap(si ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(si, serverRequest), BlueResponse.class));
    }

    /**
     * delete style
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        return getLongVariableReact(serverRequest, ID.key)
                .flatMap(id -> just(styleService.deleteStyle(id)))
                .flatMap(si ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(si, serverRequest), BlueResponse.class));
    }

    /**
     * update active style
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> updateActive(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(IdentityParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                , getAccessReact(serverRequest))
                .flatMap(tuple2 -> just(styleService.updateActiveStyle(tuple2.getT1().getId(), tuple2.getT2().getId())))
                .flatMap(si ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(si, serverRequest), BlueResponse.class));
    }

    /**
     * select styles
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> select(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PAGE_MODEL_FOR_STYLE_CONDITION_TYPE)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(styleService::selectStyleManagerInfoPageMonoByPageAndCondition)
                .flatMap(smr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(smr, serverRequest), BlueResponse.class));
    }

}
