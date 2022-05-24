package com.blue.base.router.api;

import com.blue.base.handler.api.ElementApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * element api routes
 *
 * @author liuyunfei
 */
@Configuration
public class ElementApiRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> elementApiRouter(ElementApiHandler elementApiHandler) {

        RequestPredicate pathPredicate = path("/blue-base");

        RouterFunction<ServerResponse> routerFunction = route()
                .GET("/element", elementApiHandler::select)
                .POST("/element", elementApiHandler::selectByKeys)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
