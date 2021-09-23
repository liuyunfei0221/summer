package com.blue.finance.router.api;

import com.blue.finance.handler.api.FinanceApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * 资金账户路由
 *
 * @author DarkBlue
 */
@Configuration
public class FinanceApiRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> memberRouter(FinanceApiHandler financeApiHandler) {

        RequestPredicate pathPredicate = path("/blue-finance/finance");

        RouterFunction<ServerResponse> routerFunction = route()
                .GET("/balance", financeApiHandler::getBalance)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
