package com.blue.member.router.api;

import com.blue.member.handler.api.FallbackHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * fallback
 *
 * @author liuyunfei
 */
@Configuration
public class FallbackRoute {

    @Bean
    RouterFunction<ServerResponse> fallbackApiRouter(FallbackHandler fallbackHandler) {

        RequestPredicate pathPredicate = path("/blue-member/fallback");

        RouterFunction<ServerResponse> routerFunction = route(RequestPredicates.all(), fallbackHandler::fallback);

        return nest(pathPredicate, routerFunction);
    }

}
