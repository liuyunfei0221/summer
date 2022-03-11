package com.blue.auth.router.manager;

import com.blue.auth.handler.manager.ResourceManagerHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.blue.base.constant.base.PathVariable.ID;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * auth api route
 *
 * @author DarkBlue
 */
@SuppressWarnings("DuplicatedCode")
@Configuration
public class ResourceManagerRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> resourceManagerRouter(ResourceManagerHandler resourceManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-auth/manager/resource");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("", accept(APPLICATION_JSON), resourceManagerHandler::insert)
                .PUT("", accept(APPLICATION_JSON), resourceManagerHandler::update)
                .DELETE("/{" + ID.key + "}", resourceManagerHandler::delete)
                .POST("/list", accept(APPLICATION_JSON), resourceManagerHandler::select)
                .POST("/auth", accept(APPLICATION_JSON), resourceManagerHandler::selectAuthority)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
