package com.blue.auth.router.manager;

import com.blue.auth.handler.manager.RoleManagerHandler;
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
 * role manager route
 *
 * @author liuyunfei
 */
@SuppressWarnings("DuplicatedCode")
@Configuration
public class RoleManagerRoute {

    @Bean
    RouterFunction<ServerResponse> roleManagerRouter(RoleManagerHandler roleManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-auth/manager");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/role", accept(APPLICATION_JSON), roleManagerHandler::insert)
                .PUT("/role", accept(APPLICATION_JSON), roleManagerHandler::update)
                .DELETE("/role/{" + ID.key + "}", roleManagerHandler::delete)
                .PUT("/role/default", roleManagerHandler::updateDefault)
                .POST("/roles", accept(APPLICATION_JSON), roleManagerHandler::page)
                .POST("/role/auth", accept(APPLICATION_JSON), roleManagerHandler::selectAuthority)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
