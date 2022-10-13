package com.blue.auth.router.manager;

import com.blue.auth.handler.manager.OperationManagerHandler;
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
 * operation manager route
 *
 * @author liuyunfei
 */
@SuppressWarnings("DuplicatedCode")
@Configuration
public class OperationManagerRoute {

    @Bean
    RouterFunction<ServerResponse> operationManagerRouter(OperationManagerHandler operationManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-auth/manager/operation");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/payload", accept(APPLICATION_JSON), operationManagerHandler::payload)
                .POST("/access", accept(APPLICATION_JSON), operationManagerHandler::access)
                .POST("/session", accept(APPLICATION_JSON), operationManagerHandler::session)
                .POST("/encrypted", accept(APPLICATION_JSON), operationManagerHandler::encrypted)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
