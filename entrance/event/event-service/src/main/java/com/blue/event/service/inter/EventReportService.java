package com.blue.event.service.inter;

import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

/**
 * event report service
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface EventReportService {

    /**
     * report event
     *
     * @param serverRequest
     * @return
     */
    Mono<Boolean> report(ServerRequest serverRequest);

}
