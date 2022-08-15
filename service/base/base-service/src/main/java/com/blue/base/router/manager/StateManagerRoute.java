package com.blue.base.router.manager;

import com.blue.base.handler.manager.StateManagerHandler;
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
 * state manager route
 *
 * @author liuyunfei
 */
@SuppressWarnings("DuplicatedCode")
@Configuration
public class StateManagerRoute {

    @Bean
    @SuppressWarnings("NullableProblems")
    RouterFunction<ServerResponse> stateManagerRouter(StateManagerHandler stateManagerHandler) {

        RequestPredicate pathPredicate = path("/blue-base/manager");

        RouterFunction<ServerResponse> routerFunction = route()
                .POST("/state", accept(APPLICATION_JSON), stateManagerHandler::insert)
                .PUT("/state", accept(APPLICATION_JSON), stateManagerHandler::update)
                .DELETE("/state/{" + ID.key + "}", stateManagerHandler::delete)
                .POST("/states", accept(APPLICATION_JSON), stateManagerHandler::page)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
