package com.blue.auth.router.manager;

import com.blue.auth.handler.manager.ResourceManagerHandler;
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
 * resource manager route
 *
 * @author liuyunfei
 */
@SuppressWarnings("DuplicatedCode")
@Configuration
public class ResourceManagerRoute {

    @Bean
    RouterFunction<ServerResponse> resourceManagerRouter(ResourceManagerHandler resourceManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-auth/manager");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/resource", accept(APPLICATION_JSON), resourceManagerHandler::insert)
                .PUT("/resource", accept(APPLICATION_JSON), resourceManagerHandler::update)
                .DELETE("/resource/{" + ID.key + "}", resourceManagerHandler::delete)
                .POST("/resources", accept(APPLICATION_JSON), resourceManagerHandler::page)
                .POST("/resource/auth", accept(APPLICATION_JSON), resourceManagerHandler::selectAuthority)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
