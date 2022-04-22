package com.blue.portal.router.manager;

import com.blue.portal.handler.manager.BulletinManagerHandler;
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
 * bulletin manager route
 *
 * @author liuyunfei
 */
@Configuration
public class BulletinManagerRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> bulletinManagerRouter(BulletinManagerHandler bulletinManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-media/manager");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/bulletins", accept(APPLICATION_JSON), bulletinManagerHandler::listBulletin)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
