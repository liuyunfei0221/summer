package com.blue.shine.router.api;

import com.blue.shine.handler.api.FallbackHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.all;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * @author DarkBlue
 */
@Configuration
public class FallbackRoute {

    @SuppressWarnings("NullableProblems")
    @Bean
    RouterFunction<ServerResponse> fallbackApiRouter(FallbackHandler fallbackHandler) {

        RequestPredicate pathPredicate = path("/fallback");

        RouterFunction<ServerResponse> routerFunction = route(all(), fallbackHandler::fallback);

        return nest(pathPredicate, routerFunction);
    }

}
