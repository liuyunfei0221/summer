package com.blue.media.common.request.body;

import com.blue.base.model.base.DataEvent;
import com.blue.media.component.RequestEventReporter;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * request body processor
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface ReportWithRequestBodyProcessor {

    /**
     * handle type
     *
     * @return
     */
    String getContentType();

    /**
     * handle
     *
     * @param request
     * @param exchange
     * @param chain
     * @param requestEventReporter
     * @param dataEvent
     * @return
     */
    Mono<Void> processor(ServerHttpRequest request, ServerWebExchange exchange, WebFilterChain chain, RequestEventReporter requestEventReporter, DataEvent dataEvent);

}
