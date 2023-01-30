package com.blue.risk.router.manager;

import com.blue.risk.handler.manager.RiskHitRecordManagerHandler;
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
 * risk hit record router
 *
 * @author liuyunfei
 */
@Configuration
public class RiskHitRecordManagerRoute {

    @Bean
    RouterFunction<ServerResponse> optEventRouter(RiskHitRecordManagerHandler riskHitRecordManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-risk/manager");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/records", accept(APPLICATION_JSON), riskHitRecordManagerHandler::scroll)
                .POST("/records/count", accept(APPLICATION_JSON), riskHitRecordManagerHandler::count)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}