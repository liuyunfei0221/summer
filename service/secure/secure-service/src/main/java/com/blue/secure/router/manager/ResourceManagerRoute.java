package com.blue.secure.router.manager;

import com.blue.secure.handler.manager.ResourceManagerHandler;
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
 * secure api route
 *
 * @author DarkBlue
 */
@Configuration
public class ResourceManagerRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> resourceManagerRouter(ResourceManagerHandler resourceManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-secure/manager/resource");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/list", accept(APPLICATION_JSON), resourceManagerHandler::select)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
