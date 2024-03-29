package com.blue.base.router.api;

import com.blue.base.handler.api.StateApiHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static com.blue.basic.constant.common.PathVariable.PID;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.path;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

/**
 * state api route
 *
 * @author liuyunfei
 */
@Configuration
public class StateApiRoute {

    @Bean
    RouterFunction<ServerResponse> stateApiRouter(StateApiHandler stateApiHandler) {

        RequestPredicate pathPredicate = path("/blue-base");

        RouterFunction<ServerResponse> routerFunction = route()
                .GET("/states/{" + PID.key + "}", accept(APPLICATION_JSON), stateApiHandler::selectByCountryId)
                .build();

        return nest(pathPredicate, routerFunction);
    }

}
