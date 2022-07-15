package com.blue.base.router.manager;

import com.blue.base.handler.manager.CityManagerHandler;
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
 * city manager route
 *
 * @author liuyunfei
 */
@SuppressWarnings("DuplicatedCode")
@Configuration
public class CityManagerRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> cityManagerRouter(CityManagerHandler cityManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-base/manager");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/city", accept(APPLICATION_JSON), cityManagerHandler::insert)
                .PUT("/city", accept(APPLICATION_JSON), cityManagerHandler::update)
                .DELETE("/city/{" + ID.key + "}", cityManagerHandler::delete)
                .POST("/cities", accept(APPLICATION_JSON), cityManagerHandler::select)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
