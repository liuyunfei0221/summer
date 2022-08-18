package com.blue.shine.router.manager;

import com.blue.shine.handler.manager.ShineManagerHandler;
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
 * shine manager routers
 *
 * @author liuyunfei
 */
@Configuration
public class ShineManagerRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> shineManagerRouter(ShineManagerHandler shineManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-shine/manager");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/shine", accept(APPLICATION_JSON), shineManagerHandler::insert)
                .PUT("/shine", accept(APPLICATION_JSON), shineManagerHandler::update)
                .DELETE("/shine/{" + ID.key + "}", shineManagerHandler::delete)
                .POST("/shine/page", accept(APPLICATION_JSON), shineManagerHandler::page)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
