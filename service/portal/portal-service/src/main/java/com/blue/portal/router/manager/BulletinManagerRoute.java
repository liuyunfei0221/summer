package com.blue.portal.router.manager;

import com.blue.portal.handler.manager.BulletinManagerHandler;
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
 * bulletin manager route
 *
 * @author liuyunfei
 */
@Configuration
public class BulletinManagerRoute {

    @Bean
    RouterFunction<ServerResponse> bulletinManagerRouter(BulletinManagerHandler bulletinManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-portal/manager");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/bulletin", accept(APPLICATION_JSON), bulletinManagerHandler::insert)
                .PUT("/bulletin", accept(APPLICATION_JSON), bulletinManagerHandler::update)
                .DELETE("/bulletin/{" + ID.key + "}", bulletinManagerHandler::delete)
                .POST("/bulletins", accept(APPLICATION_JSON), bulletinManagerHandler::page)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
