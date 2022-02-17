package com.blue.analyze.handler.api;

import com.blue.analyze.model.MergeSummaryParam;
import com.blue.analyze.model.SummaryParam;
import com.blue.analyze.service.inter.StatisticsService;
import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.exps.BlueException;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.base.ResponseElement.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;

/**
 * statistics api handler
 *
 * @author DarkBlue
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
                .switchIfEmpty(
                        error(() -> new BlueException(EMPTY_PARAM)))
                .flatMap(statisticsService::selectActiveSimple)
                .flatMap(count ->
                        ok()
                                .contentType(APPLICATION_JSON)
                                .body(generate(OK.code, count, serverRequest), BlueResponse.class));
    }

    /**
     * statistics merge active
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> statisticsActiveMerge(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(MergeSummaryParam.class)
                .switchIfEmpty(
                        error(() -> new BlueException(EMPTY_PARAM)))
                .flatMap(statisticsService::selectActiveMerge)
                .flatMap(count ->
                        ok()
                                .contentType(APPLICATION_JSON)
                                .body(generate(OK.code, count, serverRequest), BlueResponse.class));
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
                                .body(generate(OK.code, summary, serverRequest), BlueResponse.class));
    }

}
