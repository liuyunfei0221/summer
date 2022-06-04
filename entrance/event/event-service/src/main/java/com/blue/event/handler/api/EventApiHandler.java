package com.blue.event.handler.api;

import com.blue.base.model.common.BlueResponse;
import com.blue.event.service.inter.EventReportService;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.just;


/**
 * event report api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc"})
@Component
public final class EventApiHandler {

    private final EventReportService eventReportService;

    public EventApiHandler(EventReportService eventReportService) {
        this.eventReportService = eventReportService;
    }

    /**
     * report event
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> report(ServerRequest serverRequest) {
        return just(serverRequest)
                .flatMap(eventReportService::report)
                .flatMap(b ->
                        ok().contentType(APPLICATION_JSON)
                                .body(generate(OK.code, b, serverRequest), BlueResponse.class));
    }

}
