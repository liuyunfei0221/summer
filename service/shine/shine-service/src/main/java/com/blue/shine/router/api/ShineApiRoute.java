package com.blue.shine.router.api;

import com.blue.shine.handler.api.ShineApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * shine api routers
 *
 * @author liuyunfei
 */
@Configuration
public class ShineApiRoute {

    @Bean
    RouterFunction<ServerResponse> shineApiRouter(ShineApiHandler shineApiHandler) {

        RequestPredicate pathPredicate = path("/blue-shine");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/shines/scroll", shineApiHandler::scroll)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
