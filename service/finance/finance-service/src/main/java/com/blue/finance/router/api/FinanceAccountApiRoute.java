package com.blue.finance.router.api;

import com.blue.finance.handler.api.FinanceAccountApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * finance account api routers
 *
 * @author liuyunfei
 */
@Configuration
public class FinanceAccountApiRoute {

    @Bean
    RouterFunction<ServerResponse> financeAccountApiRouter(FinanceAccountApiHandler balanceApiHandler) {

        RequestPredicate pathPredicate = path("/blue-finance/account");

        RouterFunction<ServerResponse> routerFunction = route()
                .GET("", balanceApiHandler::get)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
