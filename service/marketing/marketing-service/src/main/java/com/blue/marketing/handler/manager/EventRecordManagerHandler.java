package com.blue.marketing.handler.manager;

import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.marketing.service.inter.EventRecordService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.marketing.constant.MarketingTypeReference.PAGE_MODEL_FOR_EVENT_RECORD_MANAGER_CONDITION_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.defer;
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
    public Mono<ServerResponse> page(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(PAGE_MODEL_FOR_EVENT_RECORD_MANAGER_CONDITION_TYPE)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(eventRecordService::selectEventRecordInfoPageByPageAndCondition)
                .flatMap(pmr ->
                        ok().contentType(APPLICATION_JSON)
                                .body(success(pmr, serverRequest), BlueResponse.class));
    }

}
