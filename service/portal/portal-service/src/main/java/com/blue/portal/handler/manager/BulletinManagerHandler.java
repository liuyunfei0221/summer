package com.blue.portal.handler.manager;

import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.portal.model.BulletinInsertParam;
import com.blue.portal.model.BulletinUpdateParam;
import com.blue.portal.service.inter.BulletinService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.reactive.AccessGetterForReactive.*;
import static com.blue.basic.common.reactive.PathVariableGetter.getLongVariableReact;
import static com.blue.basic.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.basic.constant.common.PathVariable.ID;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.ResponseElement.OK;
import static com.blue.portal.constant.PortalTypeReference.PAGE_MODEL_FOR_BULLETIN_CONDITION_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * bulletin manager handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class BulletinManagerHandler {

    private final BulletinService bulletinService;

    public BulletinManagerHandler(BulletinService bulletinService) {
        this.bulletinService = bulletinService;
    }

    /**
     * create a new bulletin
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> insert(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(BulletinInsertParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> just(bulletinService.insertBulletin(tuple2.getT1(), tuple2.getT2().getId())))
                .flatMap(bi ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, bi, serverRequest), BlueResponse.class));
    }

    /**
     * update bulletin
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(BulletinUpdateParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> just(bulletinService.updateBulletin(tuple2.getT1(), tuple2.getT2().getId())))
                .flatMap(bi ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, bi, serverRequest), BlueResponse.class));
    }

    /**
     * delete bulletin
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        return getLongVariableReact(serverRequest, ID.key)
                .flatMap(id -> just(bulletinService.deleteBulletin(id)))
                .flatMap(bi ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, bi, serverRequest), BlueResponse.class));
    }

    /**
     * select bulletin by page and condition
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> select(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PAGE_MODEL_FOR_BULLETIN_CONDITION_TYPE)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(bulletinService::selectBulletinManagerInfoPageMonoByPageAndCondition)
                .flatMap(pmr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, pmr, serverRequest), BlueResponse.class));
    }

}
