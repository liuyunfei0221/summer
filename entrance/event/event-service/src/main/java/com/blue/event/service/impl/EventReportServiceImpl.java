package com.blue.event.service.impl;

import com.blue.auth.api.model.MemberPayload;
import com.blue.event.service.inter.EventReportService;
import com.blue.jwt.common.JwtProcessor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

/**
 * event report service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "SpringJavaInjectionPointsAutowiringInspection"})
@Service
public class EventReportServiceImpl implements EventReportService {

    private final JwtProcessor<MemberPayload> jwtProcessor;

    public EventReportServiceImpl(JwtProcessor<MemberPayload> jwtProcessor) {
        this.jwtProcessor = jwtProcessor;
    }

    /**
     * report event
     *
     * @param serverRequest
     * @return
     */
    @Override
    public Mono<Boolean> report(ServerRequest serverRequest) {

        

        return Mono.just(true);
    }

}
