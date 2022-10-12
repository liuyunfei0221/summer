package com.blue.media.router.api;

import com.blue.media.handler.api.TestApiHandler;
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
 * test encrypt api route
 *
 * @author liuyunfei
 */
@Configuration
public class TestApiRoute {

    @Bean
    RouterFunction<ServerResponse> testApiRouter(TestApiHandler testApiHandler) {

        RequestPredicate pathPredicate = path("/blue-media");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/withdraw", accept(APPLICATION_JSON), testApiHandler::withdraw)
                .POST("/trd", accept(APPLICATION_JSON), testApiHandler::trd)
                .POST("/rd", accept(APPLICATION_JSON), testApiHandler::rd)
                .POST("/fd", accept(APPLICATION_JSON), testApiHandler::fd)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
