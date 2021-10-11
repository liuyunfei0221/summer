package com.blue.finance.router.api;

import com.blue.finance.handler.api.WithdrawApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * test encrypt routers
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
@Configuration
public class WithdrawApiRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> withdrawRouter(WithdrawApiHandler withdrawApiHandler) {

        RequestPredicate pathPredicate = path("/blue-finance/withdraw");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("", accept(APPLICATION_JSON), withdrawApiHandler::withdraw)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
