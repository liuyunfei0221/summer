package com.blue.base.router.manager;

import com.blue.base.handler.manager.DataMoveHandler;
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
 * @author DarkBlue
 */
@Configuration
public class DataMoveRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> dataMoveRouter(DataMoveHandler dataMoveHandler) {

        RequestPredicate pathPredicate = path("/blue-base");

        RouterFunction<ServerResponse> routerFunction = route()
                .GET("/move", accept(APPLICATION_JSON), dataMoveHandler::move)
                .GET("/region", accept(APPLICATION_JSON), dataMoveHandler::region)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
