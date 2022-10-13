package com.blue.auth.router.api;

import com.blue.auth.handler.api.AccessApiHandler;
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
 * access api route
 *
 * @author liuyunfei
 */
@Configuration
public class AccessApiRoute {

    @Bean
    RouterFunction<ServerResponse> accessApiRouter(AccessApiHandler accessApiHandler) {

        RequestPredicate pathPredicate = path("/blue-auth");

        RouterFunction<ServerResponse> routerFunction = route()
                .PUT("/access", accept(APPLICATION_JSON), accessApiHandler::updateAccess)
                .POST("/access", accept(APPLICATION_JSON), accessApiHandler::resetAccess)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
