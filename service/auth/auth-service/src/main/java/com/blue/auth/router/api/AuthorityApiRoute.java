package com.blue.auth.router.api;

import com.blue.auth.handler.api.AuthorityApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * authority api route
 *
 * @author liuyunfei
 */
@Configuration
public class AuthorityApiRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> authApiRouter(AuthorityApiHandler authorityApiHandler) {

        RequestPredicate pathPredicate = path("/blue-auth");

        RouterFunction<ServerResponse> routerFunction = route()
                .GET("/authorities", authorityApiHandler::selectAuthorities)
                .GET("/authority", authorityApiHandler::getAuthority)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
