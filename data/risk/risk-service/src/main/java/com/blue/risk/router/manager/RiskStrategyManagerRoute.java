package com.blue.risk.router.manager;

import com.blue.risk.handler.manager.RiskStrategyManagerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.blue.basic.constant.common.PathVariable.ID;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * risk strategy manager route
 *
 * @author liuyunfei
 */
@Configuration
public class RiskStrategyManagerRoute {

    @Bean
    RouterFunction<ServerResponse> riskStrategyManagerRouter(RiskStrategyManagerHandler riskStrategyManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-risk/manager");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/strategy", accept(APPLICATION_JSON), riskStrategyManagerHandler::insert)
                .PUT("/strategy", accept(APPLICATION_JSON), riskStrategyManagerHandler::update)
                .DELETE("/strategy/{" + ID.key + "}", riskStrategyManagerHandler::delete)
                .POST("/strategies", accept(APPLICATION_JSON), riskStrategyManagerHandler::page)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
