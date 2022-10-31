package com.blue.marketing.handler.api;

import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.marketing.service.inter.EventRecordService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.base.AccessGetter.getAccessReact;
import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.marketing.constant.MarketingTypeReference.SCROLL_MODEL_FOR_EVENT_RECORD_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;

/**
 * event record api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc"})
@Component
public final class EventRecordApiHandler {

    private final EventRecordService eventRecordService;

    public EventRecordApiHandler(EventRecordService eventRecordService) {
        this.eventRecordService = eventRecordService;
    }

    /**
     * select event record by scroll and current member
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> scroll(ServerRequest serverRequest) {
        return zip(serverRequest.bodyToMono(SCROLL_MODEL_FOR_EVENT_RECORD_TYPE)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM)))),
                getAccessReact(serverRequest))
                .flatMap(tuple2 -> eventRecordService.selectEventRecordInfoScrollMonoByScrollAndCursorBaseOnMemberId(tuple2.getT1(), tuple2.getT2().getId())
                )
                .flatMap(smr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(smr, serverRequest), BlueResponse.class));
    }

}
