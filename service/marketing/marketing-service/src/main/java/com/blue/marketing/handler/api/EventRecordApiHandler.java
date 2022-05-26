package com.blue.marketing.handler.api;

import com.blue.base.model.common.BlueResponse;
import com.blue.base.model.common.PageModelRequest;
import com.blue.base.model.common.PageModelResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.marketing.api.model.EventRecordInfo;
import com.blue.marketing.service.inter.EventRecordService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.AccessGetterForReactive.*;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.base.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * event record api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class EventRecordApiHandler {

    private final EventRecordService eventRecordService;

    public EventRecordApiHandler(EventRecordService eventRecordService) {
        this.eventRecordService = eventRecordService;
    }

    /**
     * select event record by page and current member
     *
     * @param serverRequest
     * @return
     */
    @SuppressWarnings("unchecked")
    public Mono<ServerResponse> listEventRecord(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(PageModelRequest.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 ->
                        (Mono<PageModelResponse<EventRecordInfo>>) eventRecordService.selectEventRecordInfoByPageAndCreator(tuple2.getT1(), tuple2.getT2().getId())
                )
                .flatMap(pmr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, pmr, serverRequest), BlueResponse.class));
    }

}
