package com.blue.analyze.router.api;

import com.blue.analyze.handler.api.StatisticsApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * statistics api routers
 *
 * @author liuyunfei
 */
@Configuration
public class StatisticsApiRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> dataRouter(StatisticsApiHandler statisticsApiHandler) {

        RequestPredicate pathPredicate = path("/blue-analyze/statistics");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("active/simple", accept(APPLICATION_JSON), statisticsApiHandler::statisticsActiveSimple)
                .POST("active/merge", accept(APPLICATION_JSON), statisticsApiHandler::statisticsActiveMerge)
                .POST("active/summary", statisticsApiHandler::statisticsActiveSummary)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
