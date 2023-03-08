package com.blue.portal.handler.manager;

import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.portal.model.NoticeInsertParam;
import com.blue.portal.model.NoticeUpdateParam;
import com.blue.portal.service.inter.NoticeService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.access.AccessGetter.getAccessReact;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.common.base.PathVariableGetter.getLongVariableReact;
import static com.blue.basic.constant.common.PathVariable.ID;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.portal.constant.PortalTypeReference.PAGE_MODEL_FOR_NOTICE_CONDITION_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * notice manager handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class NoticeManagerHandler {

    private final NoticeService noticeService;

    public NoticeManagerHandler(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    /**
     * create a new notice
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> insert(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(NoticeInsertParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> just(noticeService.insertNotice(tuple2.getT1(), tuple2.getT2().getId())))
                .flatMap(ni ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ni, serverRequest), BlueResponse.class));
    }

    /**
     * update notice
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> update(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(NoticeUpdateParam.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> just(noticeService.updateNotice(tuple2.getT1(), tuple2.getT2().getId())))
                .flatMap(ni ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ni, serverRequest), BlueResponse.class));
    }

    /**
     * delete notice
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> delete(ServerRequest serverRequest) {
        return getLongVariableReact(serverRequest, ID.key)
                .flatMap(id -> just(noticeService.deleteNotice(id)))
                .flatMap(ni ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(ni, serverRequest), BlueResponse.class));
    }

    /**
     * select notice by page and condition
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> page(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PAGE_MODEL_FOR_NOTICE_CONDITION_TYPE)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(noticeService::selectNoticeManagerInfoPageByPageAndCondition)
                .flatMap(pmr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(pmr, serverRequest), BlueResponse.class));
    }

}
