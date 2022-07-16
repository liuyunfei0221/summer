package com.blue.marketing.handler.api;

import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.exps.BlueException;
import com.blue.marketing.service.inter.EventRecordService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.reactive.AccessGetterForReactive.getAccessReact;
import static com.blue.basic.common.reactive.ReactiveCommonFunctions.success;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * event record api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "ReactiveStreamsUnusedPublisher"})
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
    public Mono<ServerResponse> select(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(PageModelRequest.class)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> eventRecordService.selectEventRecordInfoByPageAndCreator(tuple2.getT1(), tuple2.getT2().getId())
                )
                .flatMap(pmr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(pmr), BlueResponse.class));
    }

}
