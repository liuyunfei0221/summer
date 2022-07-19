package com.blue.verify.router.manager;

import com.blue.verify.handler.manager.VerifyHistoryManagerHandler;
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
 * verify history manager route
 *
 * @author liuyunfei
 */
@Configuration
public class VerifyHistoryManagerRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> verifyHistoryManagerRouter(VerifyHistoryManagerHandler verifyHistoryManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-verify/manager");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/verifyHistories", accept(APPLICATION_JSON), verifyHistoryManagerHandler::select)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
