package com.blue.analyze.handler.api;

import com.blue.analyze.model.MergeSummaryParam;
import com.blue.analyze.model.SummaryParam;
import com.blue.analyze.service.inter.StatisticsService;
import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.base.CommonFunctions.success;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.defer;
import static reactor.core.publisher.Mono.error;

/**
 * statistics api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class StatisticsApiHandler {

    private final StatisticsService statisticsService;

    public StatisticsApiHandler(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    /**
     * statistics active
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> statisticsActiveSimple(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SummaryParam.class)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(statisticsService::selectActiveSimple)
                .flatMap(count ->
                        ok()
                                .contentType(APPLICATION_JSON)
                                .body(success(count, serverRequest), BlueResponse.class));
    }

    /**
     * statistics merge active
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> statisticsActiveMerge(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(MergeSummaryParam.class)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
                .flatMap(statisticsService::selectActiveMerge)
                .flatMap(count ->
                        ok()
                                .contentType(APPLICATION_JSON)
                                .body(success(count, serverRequest), BlueResponse.class));
    }

    /**
     * statistics summary active
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> statisticsActiveSummary(ServerRequest serverRequest) {
        return statisticsService.selectActiveSummary()
                .flatMap(summary ->
                        ok()
                                .contentType(APPLICATION_JSON)
                                .body(success(summary, serverRequest), BlueResponse.class));
    }

}
