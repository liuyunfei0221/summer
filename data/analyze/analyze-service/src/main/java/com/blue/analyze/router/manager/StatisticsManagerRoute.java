package com.blue.analyze.router.manager;

import com.blue.analyze.handler.manager.StatisticsManagerHandler;
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
 * statistics manager router
 *
 * @author liuyunfei
 */
@Configuration
public class StatisticsManagerRoute {

    @Bean
    RouterFunction<ServerResponse> statisticsManagerRouter(StatisticsManagerHandler statisticsManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-analyze/manager/statistics");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/active/simple", accept(APPLICATION_JSON), statisticsManagerHandler::statisticsActiveSimple)
                .POST("/active/merge", accept(APPLICATION_JSON), statisticsManagerHandler::statisticsActiveMerge)
                .POST("/active/summary", statisticsManagerHandler::statisticsActiveSummary)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
