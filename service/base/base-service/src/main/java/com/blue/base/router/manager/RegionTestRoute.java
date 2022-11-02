package com.blue.base.router.manager;

import com.blue.base.handler.manager.RegionTestHandler;
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
 * for region test and data move
 *
 * @author liuyunfei
 */
@Configuration
public class RegionTestRoute {

    @Bean
    RouterFunction<ServerResponse> dataMoveRouter(RegionTestHandler regionTestHandler) {

        RequestPredicate pathPredicate = path("/blue-base/test");

        RouterFunction<ServerResponse> routerFunction = route()
                .GET("/move", accept(APPLICATION_JSON), regionTestHandler::move)
                .GET("/region1", accept(APPLICATION_JSON), regionTestHandler::region1)
                .GET("/region2", accept(APPLICATION_JSON), regionTestHandler::region2)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
