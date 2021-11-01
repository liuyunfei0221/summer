package com.blue.secure.router.manager;

import com.blue.secure.handler.manager.RoleManagerHandler;
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
public class RoleManagerRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> roleManagerRouter(RoleManagerHandler roleManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-secure/manager/role");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("", accept(APPLICATION_JSON), roleManagerHandler::insert)
                .POST("/list", accept(APPLICATION_JSON), roleManagerHandler::select)
                .POST("/auth", accept(APPLICATION_JSON), roleManagerHandler::selectAuthority)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
