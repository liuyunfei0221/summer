package com.blue.marketing.handler.manager;

import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.base.PageModelResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.marketing.api.model.EventRecordInfo;
import com.blue.marketing.service.inter.EventRecordService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.base.ResponseElement.OK;
import static com.blue.marketing.constant.MarketingTypeReference.PAGE_MODEL_FOR_EVENT_RECORD_CONDITION_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;

/**
 * event record manager handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class EventRecordManagerHandler {

    private final EventRecordService eventRecordService;

    public EventRecordManagerHandler(EventRecordService eventRecordService) {
        this.eventRecordService = eventRecordService;
    }

    /**
     * select event record by page and condition
     *
     * @param serverRequest
     * @return
     */
    @SuppressWarnings("unchecked")
    public Mono<ServerResponse> listEventRecord(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PAGE_MODEL_FOR_EVENT_RECORD_CONDITION_TYPE)
                        .switchIfEmpty(error(() -> new BlueException(EMPTY_PARAM)))
                .flatMap(pageModelRequest ->
                        (Mono<PageModelResponse<EventRecordInfo>>) eventRecordService.selectEventRecordInfoPageMonoByPageAndCondition(pageModelRequest)
                )
                .flatMap(pmr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, pmr, serverRequest), BlueResponse.class));
    }

}
