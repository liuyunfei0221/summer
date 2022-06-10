package com.blue.finance.router.api;

import com.blue.finance.handler.api.BalanceApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * finance api routers
 *
 * @author liuyunfei
 */
@Configuration
public class BalanceApiRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> financeApiRouter(BalanceApiHandler balanceApiHandler) {

        RequestPredicate pathPredicate = path("/blue-finance");

        RouterFunction<ServerResponse> routerFunction = route()
                .GET("/balance", balanceApiHandler::get)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
