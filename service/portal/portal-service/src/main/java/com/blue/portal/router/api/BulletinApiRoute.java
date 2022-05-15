package com.blue.portal.router.api;

import com.blue.portal.handler.api.BulletinApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.blue.base.constant.base.PathVariable.TYPE;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * bulletin api routers
 *
 * @author liuyunfei
 */
@Configuration
public class BulletinApiRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> bulletinApiRouter(BulletinApiHandler bulletinApiHandler) {

        RequestPredicate pathPredicate = path("/blue-bulletin");

        RouterFunction<ServerResponse> routerFunction = route()
                .GET("/{" + TYPE.key + "}", bulletinApiHandler::selectBulletin)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
